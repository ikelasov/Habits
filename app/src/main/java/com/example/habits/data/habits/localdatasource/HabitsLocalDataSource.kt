package com.example.habits.data.habits.localdatasource

import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsLocalDataSource @Inject constructor(
    private val habitDao: HabitDao
) {

    suspend fun getHabit(habitId: String, userId: String): HabitEntity =
        habitDao.getHabit(habitId, userId)

    fun getHabitsFlowForUser(userId: String): Flow<List<HabitEntity>> =
        habitDao.getHabitsFlowForUser(userId)

    suspend fun getHabitsWithoutRemindersSet(userId: String): List<HabitEntity> =
        habitDao.getUserHabitsWithoutRemindersSet(userId)

    suspend fun createHabit(
        habitId: String,
        userId: String,
        habitName: String,
        categoryId: String?,
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

    suspend fun updateHabit(habitEntity: HabitEntity) {
        habitDao.updateHabit(habitEntity)
    }

    suspend fun updateHabitProgress(habitId: String, updatedProgress: Int) {
        habitDao.updateCompletedRepetitions(habitId, updatedProgress)
    }
}
