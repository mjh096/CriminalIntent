package com.example.criminalintent

import com.example.criminalintent.database.CrimeDatabase

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import java.util.UUID

private const val DATABASE_NAME = "crime-database"

/**
 * Repository module for handling data operations.
 * This class provides a centralized point of access to the app's data,
 * abstracting the data sources (like databases or network services) from the rest of the app.
 *
 * It uses a Room database to store and retrieve [Crime] objects.
 * The repository is implemented as a singleton to ensure only one instance exists throughout the app.
 *
 * @property db The Room database instance.
 * @constructor Creates a new instance of CrimeRepository. This constructor is private to enforce singleton pattern.
 * @param context The application context, used to initialize the database.
 */
class CrimeRepository private constructor(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    )
        // .createFromAsset("databases/crime-database.db") // optional prepopulated DB, see note below
        .build()

    fun getCrimes(): Flow<List<Crime>> = db.crimeDao().getCrimes()
    fun getCrime(id: UUID): Flow<Crime?> = db.crimeDao().getCrime(id)

    companion object {
        @Volatile private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = CrimeRepository(context)
        }
        fun get(): CrimeRepository =
            INSTANCE ?: error("CrimeRepository must be initialized")
    }
}
