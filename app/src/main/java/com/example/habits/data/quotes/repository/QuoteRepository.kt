package com.example.habits.data.quotes.repository

import com.example.habits.data.model.quotes.FireStoreQuote
import com.example.habits.data.model.quotes.QuoteEntity
import com.example.habits.data.quotes.localdatasource.QuoteLocalDataSource
import com.example.habits.data.quotes.remotedatasource.QuoteRemoteDataSource
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val remoteDataSource: QuoteRemoteDataSource,
    private val localDataSource: QuoteLocalDataSource
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