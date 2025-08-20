package com.example.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminalintent.Crime

/**
 * The Room database for this app.
 *
 * This class defines the database configuration and serves as the app's main access point
 * to the persisted data. It declares the entities that belong in the database and the DAOs
 * that provide methods for interacting with the data.
 *
 * The `@Database` annotation marks this class as a Room database and specifies the entities
 * (tables) and the database version.
 * `exportSchema = false` is used here to avoid build warnings related to schema exportation,
 * which is not needed for this sample project. In a production app, it's good practice to
 * set up schema exportation to keep track of database versions.
 *
 * The `@TypeConverters` annotation tells Room to use the specified `CrimeTypeConverters`
 * class to convert custom types (like `Date` and `UUID`) to and from types that SQLite can store.
 *
 * This class is abstract because Room will generate an implementation of it.
 *
 * It provides an abstract method `crimeDao()` which Room will implement to return an instance
 * of `CrimeDao`. This DAO is then used to interact with the `Crime` table in the database.
 */
@Database(entities = [Crime::class], version = 1, exportSchema = false)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}