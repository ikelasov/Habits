package com.example.habits.feature_habits.common.domain

import com.example.habits.core.data.habits.repository.HabitRepository
import javax.inject.Inject

class GetHabitsFlowUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
) {
    operator fun invoke() = habitRepository.getHabitsFlow()
}