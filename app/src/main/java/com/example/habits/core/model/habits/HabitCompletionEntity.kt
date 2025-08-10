package com.example.habits.core.model.habits

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "habit_completions",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitId: String,
    val date: LocalDate,
    val completedRepetitions: Int
)