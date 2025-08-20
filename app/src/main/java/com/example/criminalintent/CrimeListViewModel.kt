package com.example.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for managing the list of crimes.
 *
 * This ViewModel holds a [StateFlow] of a list of [Crime] objects,
 * which are fetched from the [CrimeRepository]. The list of crimes is
 * observed by the UI and updates automatically when the underlying data changes.
 *
 * The `crimes` StateFlow is configured to start collecting data from the repository
 * eagerly (as soon as the ViewModel is created) and the initial value of the StateFlow is an empty list.
 * This ensures that the latest list of crimes is always available to the UI.
 */
class CrimeListViewModel : ViewModel() {

    private val repo = CrimeRepository.get()

    // Keep latest list available to the UI
    val crimes: StateFlow<List<Crime>> =
        repo.getCrimes()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
}