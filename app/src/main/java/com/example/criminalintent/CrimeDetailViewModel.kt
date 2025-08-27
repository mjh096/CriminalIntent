package com.example.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel for managing the details of a single crime.
 *
 * This ViewModel is responsible for fetching and updating a specific crime's data.
 * It observes changes to the crime from the repository and provides a StateFlow
 * for the UI to observe. It also allows for updating the crime details.
 *
 * @param crimeId The unique identifier of the crime to be displayed and managed.
 */
class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {
    private val repo = CrimeRepository.get()

    private val _crime = MutableStateFlow<Crime?>(null)
    val crime: StateFlow<Crime?> = _crime.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getCrime(crimeId).collect { _crime.value = it }
        }
    }

    /**
     * Updates the current crime object.
     *
     * @param onUpdate A lambda function that takes the current [Crime] object and returns an updated [Crime] object.
     */
    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { old -> old?.let(onUpdate) }
    }

    override fun onCleared() {
        // Optional: if batching, persist here
        _crime.value?.let { repo.updateCrime(it) }
        super.onCleared()
    }
}