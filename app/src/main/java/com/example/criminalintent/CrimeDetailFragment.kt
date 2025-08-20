package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import java.util.UUID


/**
 * This fragment displays the details of a single crime and allows the user to edit them.
 * It retrieves the crime data from the repository based on the crime ID passed as an argument
 * and populates the UI elements with the crime's details.
 *
 * The fragment observes changes to the crime data and updates the UI accordingly.
 */
class CrimeDetailFragment : Fragment(R.layout.fragment_crime_detail) {

    private lateinit var crimeId: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If you're on SDK 33+, you can use getSerializable(key, UUID::class.java)
        @Suppress("DEPRECATION")
        crimeId = requireArguments().getSerializable(ARG_CRIME_ID) as UUID
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleField = view.findViewById<EditText>(R.id.crime_title)
        val dateButton = view.findViewById<Button>(R.id.crime_date)
        val solvedBox  = view.findViewById<CheckBox>(R.id.crime_solved)

        val repo = CrimeRepository.get()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repo.getCrime(crimeId).collect { crime ->
                    if (crime != null) {
                        titleField.setText(crime.title)
                        dateButton.text = crime.date.toString()
                        solvedBox.isChecked = crime.isSolved
                    }
                }
            }
        }
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        fun newInstance(id: UUID) = CrimeDetailFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_CRIME_ID, id) }
        }
    }
}