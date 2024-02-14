package com.example.habits.data.localdatasource.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * from habits_table")
    fun getHabitsFlow(): Flow<List<HabitEntity>>

    @Query("SELECT * from habits_table where id=:habitId")
    fun getHabitFlow(habitId: Int): Flow<HabitEntity>

    @Query("SELECT * from habits_table where id=:habitId")
    fun getHabit(habitId: Int): HabitEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("DELETE from habits_table")
    suspend fun deleteAllHabits()

    @Query("DELETE from habits_table WHERE id=:habitId")
    suspend fun deleteHabit(habitId: Int)
}
