package com.example.habits.data.localdatasource.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * from habits_table")
    fun getHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * from habits_table where id=:habitId")
    fun getHabit(habitId: Int): Flow<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHabit(habit: HabitEntity)

    @Query("DELETE from habits_table")
    suspend fun deleteAllHabits()

    @Query("DELETE from habits_table WHERE id=:habitId")
    suspend fun deleteHabit(habitId: Int)
}
