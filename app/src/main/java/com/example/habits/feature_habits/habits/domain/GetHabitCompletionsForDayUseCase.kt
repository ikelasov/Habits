package com.example.habits.feature_habits.habits.domain

import com.example.habits.core.data.habits.repository.HabitRepository
import java.time.LocalDate
import javax.inject.Inject

class GetHabitCompletionsForDayUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
) {

    operator fun invoke(date: LocalDate) =
        habitRepository.getHabitCompletionForDate(date)
}