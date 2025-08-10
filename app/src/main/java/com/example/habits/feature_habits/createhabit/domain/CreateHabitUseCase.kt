package com.example.habits.feature_habits.createhabit.domain

import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.data.habits.repository.HabitRepository
import com.example.habits.core.data.repository.HabitRemindersRepository
import com.example.habits.core.model.habits.HabitPriorityLevel
import com.example.habits.feature_habits.createhabit.exception.CreateHabitMissingFields
import com.example.habits.feature_habits.createhabit.exception.CreateHabitMissingFieldsException
import java.time.LocalTime
import javax.inject.Inject

class CreateHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val habitRemindersRepository: HabitRemindersRepository,
) {

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