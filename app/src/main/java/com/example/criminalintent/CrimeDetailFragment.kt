package com.example.criminalintent

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.content.FileProvider

class CrimeDetailFragment : Fragment(R.layout.fragment_crime_detail) {

    private val args: CrimeDetailFragmentArgs by navArgs()
    private val vm: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    private var currentPhotoView: ImageView? = null
    private var latestUri: Uri? = null

    private val pickContact = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        if (uri != null) {
            readContactName(uri)?.let { name ->
                vm.updateCrime { it.copy(suspect = name) }
            }
        }
    }

    private val takePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            latestUri?.let { uri ->
                // Persist the file name to the crime and show the image
                val fileName = uri.lastPathSegment ?: "photo.jpg"
                vm.updateCrime { it.copy(photoFileName = fileName) }
                currentPhotoView?.let { loadPhotoInto(it, uri) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleField     = view.findViewById<EditText>(R.id.crime_title)
        val dateButton     = view.findViewById<Button>(R.id.crime_date)
        val solvedBox      = view.findViewById<CheckBox>(R.id.crime_solved)
        val sendReport     = view.findViewById<Button>(R.id.send_report)
        val chooseSuspect  = view.findViewById<Button>(R.id.choose_suspect)
        val cameraBtn      = view.findViewById<Button>(R.id.camera)
        val photoView      = view.findViewById<ImageView>(R.id.photo)
        currentPhotoView   = photoView

        // Enable/disable choose suspect based on availability
        val probe = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        val canHandle = requireContext().packageManager
            .resolveActivity(probe, 0) != null
        chooseSuspect.isEnabled = canHandle
        chooseSuspect.setOnClickListener { pickContact.launch(null) }

        sendReport.setOnClickListener {
            vm.crime.value?.let { sendReport(buildCrimeReport(it)) }
        }

        // Camera button
        cameraBtn.setOnClickListener {
            val fileName = "IMG_${args.crimeId}.jpg"
            val uri = getPhotoUri(fileName)
            latestUri = uri
            takePicture.launch(uri)
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

                    // Add Suspect Contact
                    val suspect = crime.suspect?.takeIf { it.isNotBlank() }.orEmpty()
                    chooseSuspect.text = if (suspect.isNotEmpty())
                        suspect
                    else
                        getString(R.string.choose_suspect)

                    // Load Photo if available
                    val photoName = crime.photoFileName?.takeIf { it.isNotBlank() }
                    if (photoName != null) {
                        val uri = getPhotoUri(photoName)
                        loadPhotoInto(photoView, uri)
                    } else {
                        photoView.setImageDrawable(null)
                    }
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

    private fun loadPhotoInto(imageView: ImageView, uri: Uri) {
        imageView.post {
            val input = requireContext().contentResolver.openInputStream(uri) ?: return@post
            val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(input, null, opts)
            input.close()

            val targetW = imageView.width.coerceAtLeast(1)
            val targetH = imageView.height.coerceAtLeast(1)
            val scale = maxOf(1, minOf(opts.outWidth / targetW, opts.outHeight / targetH))

            val input2 = requireContext().contentResolver.openInputStream(uri) ?: return@post
            val opts2 = BitmapFactory.Options().apply { inSampleSize = scale }
            val bmp = BitmapFactory.decodeStream(input2, null, opts2)
            input2.close()
            imageView.setImageBitmap(bmp)
        }
    }

    private fun getPhotoUri(fileName: String): Uri {
        val imagesDir = File(requireContext().filesDir, "images").apply { mkdirs() }
        val file = File(imagesDir, fileName)
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
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
