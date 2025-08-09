package com.example.habits.data.localdatasource.habits

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryEntity
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(
    tableName = "user_habits_table",
    foreignKeys = [
        ForeignKey(
            entity = HabitCategoryEntity::class,
            parentColumns = ["id"], // Points to the String 'id' in HabitCategoryEntity
            childColumns = ["categoryId"], // Matches the String 'categoryId' in this entity
            onDelete = ForeignKey.SET_NULL // If a category is deleted, habits become uncategorized
        )
    ],
    // Adding an index on the foreign key column is good practice for query performance
    indices = [Index(value = ["categoryId"])]
)
data class HabitEntity(
    @PrimaryKey
    val id: String, // The ID will come from Firestore
    val userId: String, // To scope habits to a specific user
    val categoryId: String?, // Now a String to match Firestore's ID and nullable for SET_NULL
    val name: String,
    val timeOfTheDay: TimeOfTheDay,
    val daysToRepeat: List<DaysOfWeek>,
    val repetitionsPerDay: Int,
    val completedRepetitions: Int,
    val priorityLevel: HabitPriorityLevel,
    val hasSetReminder: Boolean = false,
    val reminderTimes: List<LocalTime>,
    val createdAt: Long // Timestamp for creation date
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
    NOUN("Noun"), // Note: This might be a typo for "NOON"
    EVENING("Evening"),
    ALL_DAY("All day"),
}

enum class HabitPriorityLevel(val value: String) {
    TOP_PRIORITY("Top priority"),
    HIGH_PRIORITY("High priority"),
    MEDIUM_PRIORITY("Medium priority"),
    LOW_PRIORITY("Low priority"),
}