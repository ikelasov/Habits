package com.example.habits.core.data.habits.repository

import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.data.habits.localdatasource.HabitLocalDataSource
import com.example.habits.core.data.habits.remotedatasource.HabitRemoteDataSource
import com.example.habits.core.model.habits.HabitEntity
import com.example.habits.core.model.habits.HabitPriorityLevel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val localDataSource: HabitLocalDataSource,
    private val remoteDataSource: HabitRemoteDataSource,
    firebaseAuth: FirebaseAuth
) {
    private val userId by lazy { firebaseAuth.currentUser?.uid!! }

    fun getHabitsFlow(): Flow<List<HabitEntity>> =
        localDataSource.getHabitsFlowForUser(userId)

    suspend fun getHabitsWithoutRemindersSet(): List<HabitEntity> =
        localDataSource.getHabitsWithoutRemindersSet(userId)

    suspend fun getHabit(habitId: String): HabitEntity =
        localDataSource.getHabit(habitId, userId)

    suspend fun createHabit(
        habitName: String,
        categoryId: String,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {
        remoteDataSource.createHabitAndGetDocId(
            userId,
            habitName,
            categoryId,
            daysToRepeat,
            repetitionsPerDay,
            priorityLevel,
            reminderTime
        )
    }

    suspend fun updateHabitProgress(habitId: String, updatedProgress: Int) =
        remoteDataSource.updateHabitProgress(habitId, updatedProgress, userId)

    suspend fun updateHabitRemindersSet(habitId: String) =
        remoteDataSource.updateHabitReminderSet(habitId, userId)
}
