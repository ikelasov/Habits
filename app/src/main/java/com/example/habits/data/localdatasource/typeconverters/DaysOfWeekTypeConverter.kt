package com.example.habits.data.localdatasource.typeconverters

import androidx.room.TypeConverter
import com.example.habits.data.localdatasource.habits.DaysOfWeek

class DaysOfWeekTypeConverter {
    @TypeConverter
    fun fromList(list: List<DaysOfWeek>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<DaysOfWeek> {
        return data.split(",").map { DaysOfWeek.valueOf(it) }
    }
}
