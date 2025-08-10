package com.example.habits.feature_habits.habits.domain

import com.example.habits.core.data.habits.repository.HabitRepository
import javax.inject.Inject

class UpdateHabitProgressUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
) {

    suspend operator fun invoke(
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
}