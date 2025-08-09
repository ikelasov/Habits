package com.example.habits.domain

import com.example.habits.data.model.quotes.QuoteEntity
import com.example.habits.data.habits.repository.HabitsRepository
import com.example.habits.data.quotes.repository.QuotesRepository
import javax.inject.Inject

class QuoteUseCase @Inject constructor(
    private val quotesRepository: QuotesRepository
) {
    suspend fun getRandomQuote(): QuoteEntity? = quotesRepository.getRandomQuote()
}