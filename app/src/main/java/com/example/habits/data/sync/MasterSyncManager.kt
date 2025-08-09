package com.example.habits.data.sync

import com.example.habits.data.habitcategories.repository.CategoryRepository
import com.example.habits.data.habits.repository.HabitRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MasterSyncManager @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val habitRepository: HabitRepository,
    private val externalScope: CoroutineScope
) {

    private var syncJob: Job? = null

    fun startSync(userId: String) {
        if (syncJob?.isActive == true) return

        syncJob = externalScope.launch {
            // 1. Start listening for categories and wait for the initial sync to complete.
            categoryRepository.startListeningForCategoryChanges(userId)
            categoryRepository.initialSyncComplete.first() // This will suspend until the first item is emitted

            // 2. Once the initial category sync is complete, start listening for habits.
            // We do this because we first need the categories synced as they include the foreign key for the Habits
            habitRepository.startListeningForHabitChanges(userId)
        }
    }

    fun stopSync() {
        // Stop both listeners to clean up resources
        habitRepository.stopListeningForHabitChanges()
        categoryRepository.stopListeningForCategoryChanges()
        syncJob?.cancel()
        syncJob = null
    }
}
