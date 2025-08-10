package com.example.habits.core.data.habits.sync

import android.util.Log
import com.example.habits.core.data.habits.localdatasource.HabitLocalDataSource
import com.example.habits.core.data.habits.remotedatasource.HabitRemoteDataSource
import com.example.habits.core.model.habits.toHabitEntity
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitSyncer @Inject constructor(
    private val remoteDataSource: HabitRemoteDataSource,
    private val localDataSource: HabitLocalDataSource,
    private val externalScope: CoroutineScope
) {

    private var habitListenerRegistration: ListenerRegistration? = null
    private var currentUserId: String? = null

    fun startListeningForHabitChanges(userId: String) {
        if (currentUserId == userId && habitListenerRegistration != null) {
            // Already listening for this user
            return
        }
        stopListeningForHabitChanges()
        currentUserId = userId

        habitListenerRegistration = remoteDataSource.listenToRemoteHabits(
            userId = userId,
            onDataChanged = { firestoreHabits ->
                externalScope.launch {
                    try {
                        val habitEntities = firestoreHabits.map { it.toHabitEntity() }
                        localDataSource.replaceAllHabitsForUser(userId, habitEntities)
                    } catch (e: Exception) {
                        Log.e(
                            "HabitSyncer",
                            "Error processing habit changes for user $userId: ${e.message}",
                            e
                        )
                    }
                }
            },
            onError = { exception ->
                Log.e("HabitSyncer", "Error listening to habit changes for user $userId", exception)
            }
        )
    }

    fun stopListeningForHabitChanges() {
        habitListenerRegistration?.remove()
        habitListenerRegistration = null
        currentUserId = null
    }
}
