package com.example.habits.data.habits.repository

import android.util.Log // Keep for other methods if used, or remove if not.
import com.example.habits.data.habitscategory.localdatasource.CategoryLocalDataSource
import com.example.habits.data.habits.localdatasource.HabitsLocalDataSource
import com.example.habits.data.habits.remotedatasource.HabitRemoteDataSource
import com.example.habits.data.model.habits.DaysOfWeek
import com.example.habits.data.model.habits.HabitEntity
import com.example.habits.data.model.habits.HabitPriorityLevel
// import com.example.habits.data.model.habits.toHabitEntity; // Already in HabitSyncer if needed there
import com.google.firebase.auth.FirebaseAuth
// import com.google.firebase.firestore.ListenerRegistration; // Moved to HabitSyncer
// import kotlinx.coroutines.CoroutineScope; // Moved to HabitSyncer for listener logic
import kotlinx.coroutines.flow.Flow
// import kotlinx.coroutines.flow.first; // If not used by other methods
// import kotlinx.coroutines.launch; // Moved to HabitSyncer for listener logic
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitsLocalDataSource: HabitsLocalDataSource,
    private val habitRemoteDataSource: HabitRemoteDataSource,
    firebaseAuth: FirebaseAuth
) {
    private val userId by lazy { firebaseAuth.currentUser?.uid!! }

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
        habitRemoteDataSource.createHabitAndGetDocId(
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
        // This method only updates local. If remote update is needed, it's missing.
        // Or, if this is for local-only changes, its name could be more specific.
        // Syncer won't pick this up unless there's a corresponding remote update.
        habitsLocalDataSource.updateHabit(updatedHabitEntity)
    }
}
