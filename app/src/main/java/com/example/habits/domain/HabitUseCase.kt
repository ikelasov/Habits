package com.example.habits.domain

import com.example.habits.data.habits.repository.HabitRepository
import com.example.habits.data.model.habits.DaysOfWeek
import com.example.habits.data.model.habits.HabitEntity
import com.example.habits.data.model.habits.HabitPriorityLevel
import com.example.habits.data.repository.HabitRemindersRepository
import com.example.habits.exception.CreateHabitMissingFields
import com.example.habits.exception.CreateHabitMissingFieldsException
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import javax.inject.Inject

class HabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val habitRemindersRepository: HabitRemindersRepository,
) {
    fun getHabitsFlow(): Flow<List<HabitEntity>> {
        return habitRepository.getHabitsFlow()
    }

    suspend fun createHabit(
        habitName: String,
        categoryId: String?,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {
        throwIfMissingFields(habitName, daysToRepeat, categoryId)

        habitRepository.createHabit(
            habitName = habitName,
            categoryId = categoryId!!,
            daysToRepeat = daysToRepeat,
            repetitionsPerDay = repetitionsPerDay,
            priorityLevel = priorityLevel,
            reminderTime = reminderTime
        )

        habitRemindersRepository.createNonSetReminders()
    }

    suspend fun updateProgress(
        habitId: String,
        progressUpdateValue: Int,
    ) {
        val habit = habitRepository.getHabit(habitId)
        if (habit.completedRepetitions == 0 && progressUpdateValue < 0) {
            return
        }
        val updatedProgress = habit.completedRepetitions + progressUpdateValue
        if (updatedProgress > habit.repetitionsPerDay) {
            return
        }

        habitRepository.updateHabitProgress(habitId, updatedProgress)
    }

    private fun throwIfMissingFields(
        habitName: String,
        daysToRepeat: List<DaysOfWeek>,
        categoryId: String?,
    ) {
        val missingFields = mutableListOf<CreateHabitMissingFields>()
        if (habitName.isEmpty()) {
            missingFields.add(CreateHabitMissingFields.HABIT_NAME)
        }
        if (daysToRepeat.isEmpty()) {
            missingFields.add(CreateHabitMissingFields.DAYS_TO_REPEAT)
        }
        if (categoryId == null) {
            missingFields.add(CreateHabitMissingFields.HABIT_CATEGORY)
        }

        if (missingFields.isNotEmpty()) {
            throw CreateHabitMissingFieldsException(missingFields)
        }
    }
}
