package com.example.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {
    private val repo = CrimeRepository.get()

    private val _crime = MutableStateFlow<Crime?>(null)
    val crime: StateFlow<Crime?> = _crime.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getCrime(crimeId).collect { _crime.value = it }
        }
    }

    /** UDF-friendly updater */
    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { old -> old?.let(onUpdate) }
    }

    override fun onCleared() {
        // Optional: if batching, persist here
        _crime.value?.let { repo.updateCrime(it) }
        super.onCleared()
    }
}