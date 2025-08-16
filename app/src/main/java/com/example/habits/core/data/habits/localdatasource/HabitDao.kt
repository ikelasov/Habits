package com.example.habits.core.data.habits.localdatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.habits.core.model.habits.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * from user_habits_table WHERE userId=:userId")
    fun getHabitsFlowForUser(userId: String): Flow<List<HabitEntity>>

    @Query("SELECT * from user_habits_table WHERE hasSetReminder = 0 AND userId=:userId")
    suspend fun getUserHabitsWithoutRemindersSet(userId: String): List<HabitEntity>

    @Query("SELECT * from user_habits_table WHERE id=:habitId")
    fun getHabitFlow(habitId: String): Flow<HabitEntity>

    @Query("SELECT * from user_habits_table WHERE id=:habitId AND userId=:userId")
    suspend fun getHabit(habitId: String, userId: String): HabitEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("UPDATE user_habits_table SET completedRepetitions = :completedRepetitions WHERE id = :habitId")
    suspend fun updateCompletedRepetitions(habitId: String, completedRepetitions: Int)

    @Query("DELETE from user_habits_table WHERE userId=:userId")
    suspend fun deleteAllHabits(userId: String)

    @Query("DELETE from user_habits_table WHERE id=:habitId")
    suspend fun deleteHabit(habitId: String)

    @Upsert
    suspend fun upsertAll(habits: List<HabitEntity>)

    @Query("DELETE FROM user_habits_table WHERE userId = :userId AND id NOT IN (:remoteHabitIds)")
    suspend fun deleteMissingHabits(userId: String, remoteHabitIds: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<HabitEntity>)
}
