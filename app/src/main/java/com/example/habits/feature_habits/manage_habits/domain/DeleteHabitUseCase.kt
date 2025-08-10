package com.example.habits.feature_habits.manage_habits.domain

import com.example.habits.core.data.habits.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: String) =
        habitRepository.deleteHabit(habitId = habitId)
}