package com.example.habits.feature_habits.habits.domain

import com.example.habits.core.data.habits.repository.HabitRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UpdateHabitProgressUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
) {

    suspend operator fun invoke(
        habitId: String,
        progressUpdateValue: Int,
        date: LocalDate
    ) {
        val habit = habitRepository.getHabit(habitId)
        if (habit.completedRepetitions == 0 && progressUpdateValue < 0) {
            return
        }
        val updatedProgress = habit.completedRepetitions + progressUpdateValue
        if (updatedProgress > habit.repetitionsPerDay) {
            return
        }
        val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        habitRepository.updateHabitProgress(habitId, updatedProgress, dateString)
    }
}