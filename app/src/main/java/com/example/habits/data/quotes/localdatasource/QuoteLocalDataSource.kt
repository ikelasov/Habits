package com.example.habits.data.quotes.localdatasource

import com.example.habits.data.model.quotes.QuoteEntity
import javax.inject.Inject

class QuoteLocalDataSource @Inject constructor(private val quoteDao: QuoteDao) {

    suspend fun getRandomQuote(): QuoteEntity? = quoteDao.getRandomQuote()
    suspend fun saveQuotes(quotes: List<QuoteEntity>) {
        quoteDao.insertAll(quotes)
    }
}
