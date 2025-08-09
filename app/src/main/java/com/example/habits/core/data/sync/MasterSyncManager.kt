package com.example.habits.core.data.sync

import android.util.Log
import com.example.habits.core.data.habitcategories.sync.CategorySyncer
import com.example.habits.core.data.habits.sync.HabitSyncer
import com.example.habits.core.data.quotes.QuoteSyncer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MasterSyncManager @Inject constructor(
    private val categorySyncer: CategorySyncer,
    private val habitSyncer: HabitSyncer,
    private val quoteSyncer: QuoteSyncer,
    private val externalScope: CoroutineScope
) {

    private var syncJob: Job? = null
    private var currentUserId: String? = null

    fun startSync(userId: String) {
        if (userId == currentUserId && syncJob?.isActive == true) {
            return
        }
        stopSync()

        currentUserId = userId

        syncJob = externalScope.launch {
            try {
                quoteSyncer.syncQuotesIfWeeklyIntervalPassed()
                categorySyncer.startListening(userId)
                categorySyncer.initialSyncComplete.first()
                habitSyncer.startListeningForHabitChanges(userId)

            } catch (e: Exception) {
                Log.e("MasterSyncManager", "Sync job failed for user $userId", e)
            }
        }
    }

    fun stopSync() {
        categorySyncer.stopListening()
        habitSyncer.stopListeningForHabitChanges()

        syncJob?.cancel()
        syncJob = null
        currentUserId = null
    }
}

