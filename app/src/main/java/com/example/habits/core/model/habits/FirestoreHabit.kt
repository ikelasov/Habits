package com.example.habits.core.model.habits

import com.example.habits.core.common.model.DaysOfWeek
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

data class FirestoreHabit(
    val id: String = "",
    val userId: String = "",
    val categoryId: String = "",
    val name: String = "",
    val timeOfTheDay: String = "",
    val daysToRepeat: List<String> = emptyList(),
    val repetitionsPerDay: Int = 0,
    val completedRepetitions: Int = 0,
    val priorityLevel: String = "",
    val hasSetReminder: Boolean = false,
    val reminderTimes: List<String> = emptyList(),
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
        reminderTimes = reminderTimes?.map { LocalTime.parse(it, timeFormatter) } ?: emptyList(),
        createdAt = createdAt?.time ?: System.currentTimeMillis()
    )
}
