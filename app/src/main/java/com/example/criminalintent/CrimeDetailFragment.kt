package com.example.criminalintent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch


/**
 * This fragment displays the details of a single crime and allows the user to edit them.
 * It retrieves the crime data from the repository based on the crime ID passed as an argument
 * and populates the UI elements with the crime's details.
 *
 * The fragment observes changes to the crime data and updates the UI accordingly.
 */
class CrimeDetailFragment : Fragment(R.layout.fragment_crime_detail) {

    private val args: CrimeDetailFragmentArgs by navArgs()
    private val vm: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    private val pickContact = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        if (uri != null) {
            readContactName(uri)?.let { name ->
                vm.updateCrime { it.copy(suspect = name) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleField = view.findViewById<EditText>(R.id.crime_title)
        val dateButton = view.findViewById<Button>(R.id.crime_date)
        val solvedBox  = view.findViewById<CheckBox>(R.id.crime_solved)
        val sendReport = view.findViewById<Button>(R.id.send_report)
        val chooseSuspect = view.findViewById<Button>(R.id.choose_suspect)
        val probeIntent = pickContact.contract.createIntent(requireContext(), null)
        val canHandle = probeIntent.resolveActivity(requireContext().packageManager) != null
        chooseSuspect.isEnabled = canHandle

        chooseSuspect.setOnClickListener { pickContact.launch(null)}

        sendReport.setOnClickListener {
            vm.crime.value?.let { sendReport(buildCrimeReport(it)) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.crime.collect { crime ->
                    crime ?: return@collect

                    if (titleField.text.toString() != crime.title) {
                        titleField.setText(crime.title)
                    }
                    dateButton.text = crime.date.toString()
                    if (solvedBox.isChecked != crime.isSolved) {
                        solvedBox.isChecked = crime.isSolved
                    }

                    // Reflect suspect on the button
                    chooseSuspect.text = if (crime.suspect.isNotBlank())
                        crime.suspect
                    else
                        getString(R.string.choose_suspect)
                }
            }
        }

        // UI → VM updates
        titleField.doOnTextChanged { text, _, _, _ ->
            vm.updateCrime { it.copy(title = text.toString()) }
        }
        solvedBox.setOnCheckedChangeListener { _, checked ->
            vm.updateCrime { it.copy(isSolved = checked) }
        }
    }


    private fun readContactName(uri: Uri): String? {
        val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        requireContext().contentResolver.query(uri, projection, null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
                    return cursor.getString(idx)
                }
            }
        return null
    }

    private fun buildCrimeReport(c: Crime): String {
        val solvedStr = if (c.isSolved) "Solved" else "Unsolved"
        val suspectStr = if (c.suspect.isNotBlank()) "Suspect: ${c.suspect}" else "No suspect"
        return "Crime: ${c.title}\nDate: ${c.date}\nStatus: $solvedStr\n$suspectStr"
    }

    private fun sendReport(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_subject))
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(intent)
    }
}