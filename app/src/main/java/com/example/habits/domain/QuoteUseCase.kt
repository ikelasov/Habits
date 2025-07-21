package com.example.habits.domain

import com.example.habits.data.localdatasource.quotes.QuoteEntity
import com.example.habits.data.repository.HabitsRepository
import javax.inject.Inject

class QuoteUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository
) {
    suspend fun getRandomQuote(): QuoteEntity? {
        return habitsRepository.getRandomQuote()
    }
}