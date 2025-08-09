package com.example.habits.domain

import com.example.habits.data.model.quotes.QuoteEntity
import com.example.habits.data.quotes.repository.QuoteRepository
import javax.inject.Inject

class QuoteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend fun getRandomQuote(): QuoteEntity? = quoteRepository.getRandomQuote()
}