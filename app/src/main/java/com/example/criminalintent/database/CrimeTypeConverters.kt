package com.example.criminalintent.database

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

/**
 * Room type converters for the Crime database.
 *
 * This class provides methods to convert complex data types (Date and UUID)
 * to and from types that Room can store directly in the database.
 */
class CrimeTypeConverters {
    /**
     * Converts a [Date] object to a [Long] timestamp.
     *
     * @param date The [Date] object to convert.
     * @return The [Long] timestamp representing the [Date].
     */
    @TypeConverter fun fromDate(date: Date): Long = date.time
    /**
     * Converts a Long representing milliseconds since epoch to a Date.
     *
     * @param millis The number of milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return The corresponding Date object.
     */
    @TypeConverter fun toDate(millis: Long): Date = Date(millis)
    /**
     * Converts a [UUID] to a [String] for database storage.
     *
     * @param id The [UUID] to convert, or null.
     * @return The string representation of the [UUID], or null if the input was null.
     */
    @TypeConverter fun fromUUID(id: UUID?): String? = id?.toString()
    /**
     * Converts a String representation of a UUID to a UUID object.
     *
     * @param id The String to convert. Can be null.
     * @return The corresponding UUID object, or null if the input String was null.
     */
    @TypeConverter fun toUUID(id: String?): UUID? = id?.let(UUID::fromString)
}