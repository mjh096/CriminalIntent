package com.example.criminalintent.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import com.example.criminalintent.Crime
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime ORDER BY date DESC")
    fun getCrimes(): kotlinx.coroutines.flow.Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id = :id LIMIT 1")
    fun getCrime(id: java.util.UUID): kotlinx.coroutines.flow.Flow<Crime?>
}