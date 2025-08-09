package com.example.habits.data.quotes.repository

import com.example.habits.data.model.quotes.FireStoreQuote
import com.example.habits.data.model.quotes.QuoteEntity
import com.example.habits.data.quotes.localdatasource.QuotesLocalDataSource
import com.example.habits.data.quotes.remotedatasource.QuotesRemoteDataSource
import javax.inject.Inject

class QuotesRepository @Inject constructor(
    private val remoteDataSource: QuotesRemoteDataSource,
    private val localDataSource: QuotesLocalDataSource
) {

    suspend fun getRandomQuote(): QuoteEntity? = localDataSource.getRandomQuote()

    suspend fun fetchAndSaveQuotes() {
        val quotes = remoteDataSource.getQuotes()
        val quoteEntities = quotes.map { it.toEntity() }
        localDataSource.saveQuotes(quoteEntities)
    }

    private fun FireStoreQuote.toEntity() = QuoteEntity(
        text = text,
        author = author
    )
}