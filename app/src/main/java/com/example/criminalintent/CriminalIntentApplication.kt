package com.example.criminalintent

import android.app.Application

/**
 * Application class for CriminalIntent.
 *
 * This class is responsible for initializing the CrimeRepository when the application is created.
 */
class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}
