package com.example.criminalintent.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.criminalintent.Crime

/**
 * Data Access Object for the Crime table.
 *
 * This interface provides methods to interact with the Crime database.
 * It uses Room persistence library annotations to define database operations.
 */
@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime ORDER BY date DESC")
    fun getCrimes(): kotlinx.coroutines.flow.Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id = :id LIMIT 1")
    fun getCrime(id: java.util.UUID): kotlinx.coroutines.flow.Flow<Crime?>

    @Insert
    fun addCrime(crime: Crime)

    @Update
    fun updateCrime(crime: Crime)
}