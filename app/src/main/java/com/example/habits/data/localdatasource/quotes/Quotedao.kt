package com.example.habits.data.localdatasource.quotes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Query("SELECT * FROM quotes_table ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuote(): QuoteEntity?
}