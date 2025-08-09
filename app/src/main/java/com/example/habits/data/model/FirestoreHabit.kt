package com.example.habits.data.model

import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

data class FirestoreHabit(
    val id: String,
    val userId: String,
    val categoryId: String?,
    val name: String,
    val timeOfTheDay: String,
    val daysToRepeat: List<String>,
    val repetitionsPerDay: Int,
    val completedRepetitions: Int,
    val priorityLevel: String,
    val hasSetReminder: Boolean,
    val reminderTimes: List<String>,
    @ServerTimestamp
    val createdAt: Date? = null
)

fun FirestoreHabit.toHabitEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        userId = userId,
        categoryId = categoryId,
        name = name,
        timeOfTheDay = TimeOfTheDay.entries.first { it.value == timeOfTheDay },
        daysToRepeat = daysToRepeat.map { day ->
            DaysOfWeek.entries.first { it.value == day }
        },
        repetitionsPerDay = repetitionsPerDay,
        completedRepetitions = completedRepetitions,
        priorityLevel = HabitPriorityLevel.entries.first { it.value == priorityLevel },
        hasSetReminder = hasSetReminder,
        reminderTimes = reminderTimes.map { LocalTime.parse(it, timeFormatter) },
        createdAt = createdAt?.time ?: System.currentTimeMillis()
    )
}
