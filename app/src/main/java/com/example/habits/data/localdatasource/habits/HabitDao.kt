package com.example.habits.data.localdatasource.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * from user_habits_table")
    fun getHabitsFlow(): Flow<List<HabitEntity>>

    @Query("SELECT * from user_habits_table WHERE hasSetReminder = 0")
    suspend fun getHabitsWithoutRemindersSet(): List<HabitEntity>

    @Query("SELECT * from user_habits_table where id=:habitId")
    fun getHabitFlow(habitId: String): Flow<HabitEntity>

    @Query("SELECT * from user_habits_table where id=:habitId")
    suspend fun getHabit(habitId: String): HabitEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("DELETE from user_habits_table")
    suspend fun deleteAllHabits()

    @Query("DELETE from user_habits_table WHERE id=:habitId")
    suspend fun deleteHabit(habitId: String)
}
