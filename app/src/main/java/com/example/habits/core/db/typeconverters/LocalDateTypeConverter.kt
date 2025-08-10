package com.example.habits.core.db.typeconverters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateTypeConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }
}
