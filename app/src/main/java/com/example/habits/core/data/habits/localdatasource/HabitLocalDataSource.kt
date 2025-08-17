package com.example.habits.core.data.habits.localdatasource

import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.model.habits.HabitCompletionEntity
import com.example.habits.core.model.habits.HabitEntity
import com.example.habits.core.model.habits.HabitPriorityLevel
import com.example.habits.core.model.habits.TimeOfTheDay
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitLocalDataSource @Inject constructor(
    private val habitDao: HabitDao,
    private val habitCompletionDao: HabitCompletionDao
) {

    suspend fun getHabit(habitId: String, userId: String): HabitEntity =
        habitDao.getHabit(habitId, userId)

    fun getHabitsFlowForUser(userId: String): Flow<List<HabitEntity>> =
        habitDao.getHabitsFlowForUser(userId)

    suspend fun getHabitsWithoutRemindersSet(userId: String): List<HabitEntity> =
        habitDao.getUserHabitsWithoutRemindersSet(userId)

    fun getHabitCompletionsForDate(date: LocalDate): Flow<List<HabitCompletionEntity>> =
        habitCompletionDao.getHabitCompletionsForDate(date)

    suspend fun insertOrUpdateHabitCompletions(completions: List<HabitCompletionEntity>) =
        habitCompletionDao.insertOrUpdateHabitCompletions(completions)

    suspend fun getHabitCompletionForId(habitId: String, date: LocalDate): HabitCompletionEntity? =
        habitCompletionDao.getHabitCompletionById(habitId, date)

    suspend fun createHabit(
        habitId: String,
        userId: String,
        habitName: String,
        categoryId: String,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {
        val habitEntity = HabitEntity(
            id = habitId,
            userId = userId,
            categoryId = categoryId,
            name = habitName,
            timeOfTheDay = TimeOfTheDay.ALL_DAY,
            daysToRepeat = daysToRepeat,
            repetitionsPerDay = repetitionsPerDay,
            completedRepetitions = 0,
            priorityLevel = priorityLevel,
            hasSetReminder = false,
            reminderTimes = reminderTime?.let { listOf(it) } ?: emptyList(),
            createdAt = System.currentTimeMillis()
        )
        habitDao.createHabit(habitEntity)
    }

    suspend fun updateHabit(
        habitId: String,
        userId: String,
        habitName: String,
        categoryId: String,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?,
        hasSetReminder: Boolean
    ) {
        val existing = habitDao.getHabit(habitId, userId)
        val updated = existing.copy(
            name = habitName,
            categoryId = categoryId,
            daysToRepeat = daysToRepeat,
            repetitionsPerDay = repetitionsPerDay,
            priorityLevel = priorityLevel,
            reminderTimes = reminderTime?.let { listOf(it) } ?: emptyList(),
            hasSetReminder = hasSetReminder
        )
        habitDao.updateHabit(updated)
    }

    suspend fun updateHabitProgress(
        userId: String,
        habitId: String,
        date: LocalDate,
        updatedProgress: Int
    ) {
        val habitCompletion = HabitCompletionEntity(userId, habitId, date, updatedProgress)
        habitCompletionDao.insertOrUpdateHabitCompletion(habitCompletion)
    }

    suspend fun updateHabitRemindersSet(habitId: String, userId: String) {
        val existing = habitDao.getHabit(habitId, userId)
        habitDao.updateHabit(existing.copy(hasSetReminder = true))
    }

    suspend fun upsertAll(habits: List<HabitEntity>) {
        habitDao.upsertAll(habits)
    }

    suspend fun deleteHabit(habitId: String) {
        habitDao.deleteHabit(habitId)
    }

    suspend fun deleteMissingHabits(userId: String, remoteHabitIds: List<String>) {
        habitDao.deleteMissingHabits(userId, remoteHabitIds)
    }
}
