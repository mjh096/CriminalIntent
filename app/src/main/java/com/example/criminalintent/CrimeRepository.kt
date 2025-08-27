package com.example.criminalintent

import com.example.criminalintent.database.CrimeDatabase

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Repository module for handling data operations.
 * This class provides a centralized point of access to the app's data,
 * abstracting the data sources (like databases or network services) from the rest of the app.
 *
 * It uses a Room database to store and retrieve [Crime] objects.
 * The repository is implemented as a singleton to ensure only one instance exists throughout the app.
 *
 * @property db The Room database instance.
 * @property crimeDao The Data Access Object for crime-related database operations.
 * @constructor Creates a new instance of CrimeRepository. This constructor is private to enforce singleton pattern.
 * @param context The application context, used to initialize the database.
 */
class CrimeRepository private constructor(context: Context) {

    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val db: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        "crime-database"
    ).build()

    private val crimeDao = db.crimeDao()

    fun getCrimes(): Flow<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): Flow<Crime?> = crimeDao.getCrime(id)
    fun updateCrime(crime: Crime) {ioScope.launch {crimeDao.updateCrime(crime)}}
    companion object {
        @Volatile private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository =
            INSTANCE ?: error("CrimeRepository must be initialized")
    }
}