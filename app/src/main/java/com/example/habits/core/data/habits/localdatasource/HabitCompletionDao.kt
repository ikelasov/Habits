package com.example.habits.core.data.habits.localdatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.habits.core.model.habits.HabitCompletionEntity
import java.time.LocalDate

@Dao
interface HabitCompletionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitCompletion(habitCompletion: HabitCompletionEntity)

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND date = :date")
    suspend fun getHabitCompletionForDate(habitId: String, date: LocalDate): HabitCompletionEntity?
}