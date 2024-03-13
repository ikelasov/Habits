package com.example.habits.data.localdatasource.habits

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(tableName = "habits_table")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val timeOfTheDay: TimeOfTheDay,
    val daysToRepeat: List<DaysOfWeek>,
    val repetitionsPerDay: Int,
    val completedRepetitions: Int,
    val priorityLevel: HabitPriorityLevel,
    val hasSetReminder: Boolean = false,
    val reminderTimes: List<LocalTime>,
)

enum class DaysOfWeek(val value: String) {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday"),
    ;

    companion object {
        fun fromLocalDate(dayOfWeek: DayOfWeek): DaysOfWeek {
            return when (dayOfWeek) {
                DayOfWeek.MONDAY -> MONDAY
                DayOfWeek.TUESDAY -> TUESDAY
                DayOfWeek.WEDNESDAY -> WEDNESDAY
                DayOfWeek.THURSDAY -> THURSDAY
                DayOfWeek.FRIDAY -> FRIDAY
                DayOfWeek.SATURDAY -> SATURDAY
                DayOfWeek.SUNDAY -> SUNDAY
            }
        }
    }
}

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
