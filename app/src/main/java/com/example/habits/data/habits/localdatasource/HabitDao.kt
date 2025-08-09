package com.example.habits.data.habits.localdatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.habits.data.model.habitcategory.HabitCategoryEntity
import com.example.habits.data.model.habits.HabitEntity
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

    @Transaction
    suspend fun replaceAllForUser(userId: String, habits: List<HabitEntity>) {
        deleteAllHabits(userId)
        insertAll(habits)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<HabitEntity>)
}
