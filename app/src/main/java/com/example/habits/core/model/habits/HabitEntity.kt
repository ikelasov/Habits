package com.example.habits.core.model.habits

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.model.habitcategory.HabitCategoryEntity
import java.time.LocalTime

@Entity(
    tableName = "user_habits_table",
    foreignKeys = [
        ForeignKey(
            entity = HabitCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val categoryId: String,
    val name: String,
    val timeOfTheDay: TimeOfTheDay,
    val daysToRepeat: List<DaysOfWeek>,
    val repetitionsPerDay: Int,
    val completedRepetitions: Int,
    val priorityLevel: HabitPriorityLevel,
    val hasSetReminder: Boolean = false,
    val reminderTimes: List<LocalTime>,
    val createdAt: Long
)

enum class TimeOfTheDay(val value: String) {
    MORNING("Morning"),
    NOON("Noon"),
    EVENING("Evening"),
    ALL_DAY("All day"),
}

enum class HabitPriorityLevel(val value: String) {
    TOP_PRIORITY("Top priority"),
    HIGH_PRIORITY("High priority"),
    MEDIUM_PRIORITY("Medium priority"),
    LOW_PRIORITY("Low priority"),
}