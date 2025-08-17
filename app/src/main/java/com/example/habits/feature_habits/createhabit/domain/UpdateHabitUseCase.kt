package com.example.habits.feature_habits.createhabit.domain

import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.data.habits.repository.HabitRepository
import com.example.habits.core.data.repository.HabitRemindersRepository
import com.example.habits.core.model.habits.HabitPriorityLevel
import java.time.LocalTime
import javax.inject.Inject

class UpdateHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val habitRemindersRepository: HabitRemindersRepository
) {
    suspend operator fun invoke(
        habitId: String,
        habitName: String,
        categoryId: String?,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {
        throwIfMissingFields(habitName, daysToRepeat, categoryId)
        habitRepository.updateHabit(
            habitId = habitId,
            habitName = habitName,
            categoryId = categoryId!!,
            daysToRepeat = daysToRepeat,
            repetitionsPerDay = repetitionsPerDay,
            priorityLevel = priorityLevel,
            reminderTime = reminderTime
        )
        habitRemindersRepository.createNonSetReminders()
    }
}