package com.example.criminalintent

import java.util.UUID
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for creating [CrimeDetailViewModel] instances.
 *
 * This factory is responsible for providing a [CrimeDetailViewModel]
 * with the necessary `crimeId` dependency. It implements the
 * [ViewModelProvider.Factory] interface, allowing it to be used with
 * ViewModelProviders to create ViewModels.
 *
 * @property crimeId The unique identifier of the crime to be displayed.
 */
class CrimeDetailViewModelFactory(
    private val crimeId: UUID
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrimeDetailViewModel::class.java)) {
            return CrimeDetailViewModel(crimeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}