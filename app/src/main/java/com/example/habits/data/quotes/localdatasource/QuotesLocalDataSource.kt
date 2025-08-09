package com.example.habits.data.quotes.localdatasource

import javax.inject.Inject

class QuotesLocalDataSource @Inject constructor(private val quoteDao: QuoteDao) {

    suspend fun getRandomQuote(): QuoteEntity? = quoteDao.getRandomQuote()
    suspend fun saveQuotes(quotes: List<QuoteEntity>) {
        quoteDao.insertAll(quotes)
    }
}
