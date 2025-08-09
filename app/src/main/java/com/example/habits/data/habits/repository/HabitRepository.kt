package com.example.habits.data.habits.repository

import com.example.habits.data.habits.localdatasource.HabitLocalDataSource
import com.example.habits.data.habits.remotedatasource.HabitRemoteDataSource
import com.example.habits.data.model.habits.DaysOfWeek
import com.example.habits.data.model.habits.HabitEntity
import com.example.habits.data.model.habits.HabitPriorityLevel
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

    suspend fun updateHabitProgress(habitId: String, updatedProgress: Int) {
        remoteDataSource.updateHabitProgress(habitId, updatedProgress, userId)
        localDataSource.updateHabitProgress(habitId, updatedProgress)
    }

    // TODO handle reminders (or completely remove)
    suspend fun updateHabit(updatedHabitEntity: HabitEntity) {
        // This method only updates local. If remote update is needed, it's missing.
        // Or, if this is for local-only changes, its name could be more specific.
        // Syncer won't pick this up unless there's a corresponding remote update.
        localDataSource.updateHabit(updatedHabitEntity)
    }
}
