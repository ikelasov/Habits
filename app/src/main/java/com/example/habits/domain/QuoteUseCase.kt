package com.example.habits.domain

import com.example.habits.data.quotes.localdatasource.QuoteEntity
import com.example.habits.data.habits.repository.HabitsRepository
import com.example.habits.data.quotes.QuotesRepository
import javax.inject.Inject

class QuoteUseCase @Inject constructor(
    private val quotesRepository: QuotesRepository
) {
    suspend fun getRandomQuote(): QuoteEntity? = quotesRepository.getRandomQuote()
}