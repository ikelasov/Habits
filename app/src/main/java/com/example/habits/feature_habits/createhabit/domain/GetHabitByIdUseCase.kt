package com.example.habits.feature_habits.createhabit.domain

import com.example.habits.core.data.habits.repository.HabitRepository
import javax.inject.Inject

class GetHabitByIdUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: String) =
        habitRepository.getHabit(habitId)
}