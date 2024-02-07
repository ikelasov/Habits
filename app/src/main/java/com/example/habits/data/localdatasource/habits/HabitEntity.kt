package com.example.habits.data.localdatasource.habits

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits_table")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val comment: String,
    val timeOfTheDay: TimeOfTheDay,
    val repetitionsPerDay: Float,
    val completedRepetitions: Float,
    val priorityLevel: HabitPriorityLevel,
)

enum class TimeOfTheDay(val value: String) {
    MORNING("Morning"),
    NOUN("Noun"),
    EVENING("Evening"),
    ALL_DAY("All day"),
}

enum class HabitPriorityLevel(val value: String) {
    TOP_PRIORITY("Top priority"),
    HIGH_PRIORITY("High priority"),
    MEDIUM_PRIORITY("Medium priority"),
    LOW_PRIORITY("Low priority"),
}
