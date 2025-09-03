package com.example.criminalintent

import java.util.Date
import java.util.UUID
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single crime.
 *
 * This data class is used to store information about a crime, including its unique ID,
 * title, date of occurrence, whether it has been solved, and the filename of an associated photo.
 * It is annotated with `@Entity` to be used as a table in a Room database.
 *
 * @property id The unique identifier for the crime. Defaults to a randomly generated UUID.
 * @property title The title or description of the crime. Defaults to an empty string.
 * @property date The date the crime occurred. Defaults to the current date and time.
 * @property isSolved Indicates whether the crime has been solved. Defaults to `false`.
 * @property photoFileName The filename of the photo associated with the crime. Defaults to an empty string.
 */
@Entity(tableName = "crime")
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false,
    val suspect: String = "",
    val photoFileName: String = ""
)