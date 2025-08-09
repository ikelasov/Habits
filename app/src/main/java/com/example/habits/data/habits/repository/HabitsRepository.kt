package com.example.habits.data.habits.repository

import com.example.habits.data.habits.localdatasource.DaysOfWeek
import com.example.habits.data.habits.localdatasource.HabitEntity
import com.example.habits.data.habits.localdatasource.HabitPriorityLevel
import com.example.habits.data.habits.localdatasource.HabitsLocalDataSource
import com.example.habits.data.habits.remotedatasource.HabitsRemoteDataSource
import com.example.habits.data.localdatasource.quotes.QuoteDao
import com.example.habits.data.localdatasource.quotes.QuoteEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import javax.inject.Inject

class HabitsRepository @Inject constructor(
    private val habitsLocalDataSource: HabitsLocalDataSource,
    private val habitsRemoteDataSource: HabitsRemoteDataSource,
    private val quoteDao: QuoteDao,
    firebaseAuth: FirebaseAuth
) {

    private val userId by lazy { firebaseAuth.currentUser?.uid!! }

    fun getHabitsFlow(): Flow<List<HabitEntity>> {
        return habitsLocalDataSource.getHabitsFlowForUser(userId)
    }

    suspend fun getRandomQuote(): QuoteEntity? {
        return quoteDao.getRandomQuote()
    }

    suspend fun getHabitsWithoutRemindersSet(): List<HabitEntity> {
        return habitsLocalDataSource.getHabitsWithoutRemindersSet(userId)
    }

    suspend fun getHabit(habitId: String): HabitEntity {
        return habitsLocalDataSource.getHabit(habitId, userId)
    }

    suspend fun createHabit(
        habitName: String,
        categoryId: String?,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {
        val habitId = habitsRemoteDataSource.createHabitAndGetDocId(
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
        habitsRemoteDataSource.updateHabitProgress(habitId, updatedProgress, userId)
        habitsLocalDataSource.updateHabitProgress(habitId, updatedProgress)
    }

    // TODO handle reminders (or completely remove)
    suspend fun updateHabit(updatedHabitEntity: HabitEntity) {
        habitsLocalDataSource.updateHabit(updatedHabitEntity)
    }
}