package com.example.criminalintent

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleField = view.findViewById<EditText>(R.id.crime_title)
        val dateButton = view.findViewById<Button>(R.id.crime_date)
        val solvedBox  = view.findViewById<CheckBox>(R.id.crime_solved)

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
                }
            }
        }

        // Update Crimes
        titleField.doOnTextChanged { text, _, _, _ ->
            vm.updateCrime { it.copy(title = text.toString()) }
        }
        solvedBox.setOnCheckedChangeListener { _, checked ->
            vm.updateCrime { it.copy(isSolved = checked) }
        }
    }
}