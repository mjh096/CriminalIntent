package com.example.criminalintent

import androidx.lifecycle.ViewModel
import java.util.Date

/**
 * ViewModel for managing the list of crimes.
 *
 * This ViewModel holds a list of [Crime] objects.
 * The list is initialized with 100 sample crimes, each with a unique title,
 * a date staggered by one day from the previous, and a solved status
 * that alternates between true and false.
 */
class CrimeListViewModel : ViewModel() {
    val crimes: List<Crime> = List(100) { i ->
        Crime().apply {
            title = "Crime #$i"
            date = Date(System.currentTimeMillis() - i * 86_400_000L) // stagger dates
            isSolved = (i % 2 == 0)
        }
    }
}