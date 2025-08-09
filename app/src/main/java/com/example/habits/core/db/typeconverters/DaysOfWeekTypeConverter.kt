package com.example.habits.core.db.typeconverters

import androidx.room.TypeConverter
import com.example.habits.core.common.model.DaysOfWeek

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
