package com.example.habits.core.data.quotes.repository

import com.example.habits.core.data.quotes.localdatasource.QuoteLocalDataSource
import com.example.habits.core.data.quotes.remotedatasource.QuoteRemoteDataSource
import com.example.habits.core.model.quotes.FireStoreQuote
import com.example.habits.core.model.quotes.QuoteEntity
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val remoteDataSource: QuoteRemoteDataSource,
    private val localDataSource: QuoteLocalDataSource
) {

    suspend fun getAllQuotes(): List<QuoteEntity> = localDataSource.getAllQuotes()
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