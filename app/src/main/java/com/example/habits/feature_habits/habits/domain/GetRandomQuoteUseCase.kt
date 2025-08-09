package com.example.habits.feature_habits.habits.domain

import com.example.habits.core.data.quotes.repository.QuoteRepository
import com.example.habits.core.model.quotes.QuoteEntity
import javax.inject.Inject

class GetRandomQuoteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(): QuoteEntity? = quoteRepository.getRandomQuote()
}