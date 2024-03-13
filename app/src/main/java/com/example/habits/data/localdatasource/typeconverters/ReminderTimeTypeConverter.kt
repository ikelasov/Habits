package com.example.habits.data.localdatasource.typeconverters

import androidx.room.TypeConverter
import java.time.LocalTime

class ReminderTimeTypeConverter {
    @TypeConverter
    fun fromLocalTimeList(localTimes: List<LocalTime>?): String {
        if (localTimes.isNullOrEmpty()) return ""

        val stringBuilder = StringBuilder()

        localTimes.forEachIndexed { index, time ->
            stringBuilder.append(time.toString())
            if (index < localTimes.lastIndex) {
                stringBuilder.append(",")
            }
        }

        return stringBuilder.toString()
    }

    @TypeConverter
    fun toLocalTimeList(data: String?): List<LocalTime> {
        if (data.isNullOrEmpty()) return emptyList()

        val times = data.split(",")
        val localTimes = mutableListOf<LocalTime>()
        times.forEach {
            localTimes.add(LocalTime.parse(it))
        }

        return localTimes
    }
}
