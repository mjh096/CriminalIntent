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
import androidx.navigation.fragment.navArgs


/**
 * This fragment displays the details of a single crime and allows the user to edit them.
 * It retrieves the crime data from the repository based on the crime ID passed as an argument
 * and populates the UI elements with the crime's details.
 *
 * The fragment observes changes to the crime data and updates the UI accordingly.
 */
class CrimeDetailFragment : Fragment(R.layout.fragment_crime_detail) {

    private val args: CrimeDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleField = view.findViewById<EditText>(R.id.crime_title)
        val dateButton = view.findViewById<Button>(R.id.crime_date)
        val solvedBox  = view.findViewById<CheckBox>(R.id.crime_solved)

        val repo = CrimeRepository.get()
        val crimeId = args.crimeId

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repo.getCrime(crimeId).collect { crime ->
                    crime?.let {
                        if (titleField.text.toString() != it.title) {
                            titleField.setText(it.title)
                        }
                        dateButton.text = it.date.toString()
                        if (solvedBox.isChecked != it.isSolved) {
                            solvedBox.isChecked = it.isSolved
                        }
                    }
                }
            }
        }
    }
}