package com.example.habits.data.sync

import android.util.Log
import com.example.habits.data.habitcategories.sync.CategorySyncer
import com.example.habits.data.habits.repository.HabitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MasterSyncManager @Inject constructor(
    private val categorySyncer: CategorySyncer,
    private val habitRepository: HabitRepository,
    private val externalScope: CoroutineScope
) {

    private var syncJob: Job? = null
    private var currentUserId: String? = null

    fun startSync(userId: String) {
        if (userId == currentUserId && syncJob?.isActive == true) {
            Log.d("MasterSyncManager", "Sync already active for user: $userId")
            return
        }
        stopSync()

        currentUserId = userId

        syncJob = externalScope.launch {
            try {
                categorySyncer.startListening(userId)
                categorySyncer.initialSyncComplete.first()
                habitRepository.startListeningForHabitChanges(userId)

            } catch (e: Exception) {
                Log.e("MasterSyncManager", "Sync job failed or cancelled for user $userId", e)
            }
        }
    }

    fun stopSync() {
        habitRepository.stopListeningForHabitChanges()
        categorySyncer.stopListening()

        syncJob?.cancel()
        syncJob = null
        currentUserId = null
    }
}
