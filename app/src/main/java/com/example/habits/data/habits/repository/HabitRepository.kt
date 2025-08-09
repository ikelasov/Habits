package com.example.habits.data.habits.repository

import android.util.Log
import com.example.habits.data.habitscategory.localdatasource.CategoryLocalDataSource
import com.example.habits.data.habits.localdatasource.HabitsLocalDataSource
import com.example.habits.data.habits.remotedatasource.HabitRemoteDataSource
import com.example.habits.data.model.habits.DaysOfWeek
import com.example.habits.data.model.habits.HabitEntity
import com.example.habits.data.model.habits.HabitPriorityLevel
import com.example.habits.data.model.habits.toHabitEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitsLocalDataSource: HabitsLocalDataSource,
    private val habitRemoteDataSource: HabitRemoteDataSource,
    private val categoryLocalDataSource: CategoryLocalDataSource, // Used for defensive filtering
    private val externalScope: CoroutineScope,
    firebaseAuth: FirebaseAuth
) {
    private val userId by lazy { firebaseAuth.currentUser?.uid!! }

    private var habitsListener: ListenerRegistration? = null

    fun startListeningForHabitChanges(userId: String) {
        if (habitsListener != null) return

        habitsListener = habitRemoteDataSource.listenToRemoteHabits(
            userId = userId,
            onDataChanged = { firestoreHabits ->
                externalScope.launch {
                    val allHabitEntities = firestoreHabits.map { it.toHabitEntity() }
                    habitsLocalDataSource.replaceAllHabitsForUser(userId, allHabitEntities)
                }
            },
            onError = { exception ->
                Log.e(
                    "HabitRepository",
                    "Error listening to habit changes for user $userId",
                    exception
                )
            }
        )
    }

    fun stopListeningForHabitChanges() {
        habitsListener?.remove()
        habitsListener = null
    }

    fun getHabitsFlow(): Flow<List<HabitEntity>> =
        habitsLocalDataSource.getHabitsFlowForUser(userId)

    suspend fun getHabitsWithoutRemindersSet(): List<HabitEntity> =
        habitsLocalDataSource.getHabitsWithoutRemindersSet(userId)

    suspend fun getHabit(habitId: String): HabitEntity =
        habitsLocalDataSource.getHabit(habitId, userId)

    suspend fun createHabit(
        habitName: String,
        categoryId: String,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {
        val habitId = habitRemoteDataSource.createHabitAndGetDocId(
            userId,
            habitName,
            categoryId,
            daysToRepeat,
            repetitionsPerDay,
            priorityLevel,
            reminderTime
        )
        habitsLocalDataSource.createHabit(
            habitId,
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
        habitRemoteDataSource.updateHabitProgress(habitId, updatedProgress, userId)
        habitsLocalDataSource.updateHabitProgress(habitId, updatedProgress)
    }

    // TODO handle reminders (or completely remove)
    suspend fun updateHabit(updatedHabitEntity: HabitEntity) {
        habitsLocalDataSource.updateHabit(updatedHabitEntity)
    }
}
