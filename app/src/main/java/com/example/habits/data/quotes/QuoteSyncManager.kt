package com.example.habits.data.quotes

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import com.example.habits.data.quotes.repository.QuoteRepository

@Singleton
class QuoteSyncManager @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val sharedPreferences: SharedPreferences,
    private val applicationScope: CoroutineScope
) {

    companion object {
        private const val PREF_LAST_QUOTE_SYNC_TIMESTAMP = "last_quote_sync_timestamp"
        private const val SYNC_INTERVAL_DAYS = 7L
    }

    fun syncQuotesIfWeeklyIntervalPassed() {
        val lastSyncTimestamp = sharedPreferences.getLong(PREF_LAST_QUOTE_SYNC_TIMESTAMP, 0L)
        val currentTime = System.currentTimeMillis()
        val oneWeekInMillis = TimeUnit.DAYS.toMillis(SYNC_INTERVAL_DAYS)

        if (currentTime - lastSyncTimestamp >= oneWeekInMillis || lastSyncTimestamp == 0L) {
            applicationScope.launch {
                try {
                    quoteRepository.fetchAndSaveQuotes()
                    sharedPreferences.edit {
                        putLong(PREF_LAST_QUOTE_SYNC_TIMESTAMP, System.currentTimeMillis())
                    }
                } catch (e: Exception) {
                    // TODO: Add proper error handling (e.g., logging)
                    e.printStackTrace()
                }
            }
        }
    }
}
