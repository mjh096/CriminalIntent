package com.example.criminalintent

import java.util.Date
import java.util.UUID

/**
 * Represents a single crime.
 *
 * @property id The unique identifier for the crime.
 * @property title The title or description of the crime.
 * @property date The date the crime occurred.
 * @property isSolved Indicates whether the crime has been solved.
 */
class Crime {
    val id: UUID = UUID.randomUUID()
    var title: String = ""
    var date: Date = Date()
    var isSolved: Boolean = false
}