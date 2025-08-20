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
    @TypeConverter fun fromDate(date: Date): Long = date.time
    @TypeConverter fun toDate(millis: Long): Date = Date(millis)

    @TypeConverter fun fromUUID(id: UUID?): String? = id?.toString()
    @TypeConverter fun toUUID(id: String?): UUID? = id?.let(UUID::fromString)
}