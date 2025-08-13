package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener

/**
 * This fragment displays the details of a single crime and allows the user to edit them.
 */
class CrimeDetailFragment : Fragment() {

    /**
     * The crime being displayed.
     */
    private lateinit var crime: Crime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crime_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val titleField = view.findViewById<EditText>(R.id.crime_title)
        val dateButton = view.findViewById<Button>(R.id.crime_date)
        val solvedCheckBox = view.findViewById<CheckBox>(R.id.crime_solved)

        titleField.setText(crime.title)
        titleField.addTextChangedListener { text -> crime.title = text?.toString().orEmpty()}
        dateButton.text = crime.date.toString()
        solvedCheckBox.isChecked = crime.isSolved
        solvedCheckBox.setOnCheckedChangeListener { _, isChecked -> crime.isSolved = isChecked }
    }
}
