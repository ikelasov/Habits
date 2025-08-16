package com.example.habits.core.data.habits.sync

import android.util.Log
import com.example.habits.core.data.habits.localdatasource.HabitLocalDataSource
import com.example.habits.core.data.habits.remotedatasource.HabitRemoteDataSource
import com.example.habits.core.model.habits.toHabitCompletionEntity
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
    private var habitCompletionListenerRegistration: ListenerRegistration? = null
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
                        localDataSource.deleteMissingHabits(userId, habitEntities.map { it.id })
                        localDataSource.upsertAll(habitEntities)
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

        habitCompletionListenerRegistration = remoteDataSource.listenToAllHabitCompletions(
            userId = userId,
            onDataChanged = { firestoreCompletions ->
                externalScope.launch {
                    try {
                        // We will need a way to map FirestoreHabitCompletion to HabitCompletionEntity
                        // and a method in the local data source to save them.
                        val completionEntities =
                            firestoreCompletions.mapNotNull { it.toHabitCompletionEntity() }
                        localDataSource.insertOrUpdateHabitCompletions(completionEntities)
                    } catch (e: Exception) {
                        Log.e(
                            "HabitSyncer",
                            "Error processing habit completion changes for user $userId: ${e.message}",
                            e
                        )
                    }
                }
            },
            onError = { exception ->
                Log.e(
                    "HabitSyncer",
                    "Error listening to habit completion changes for user $userId",
                    exception
                )
            }
        )
    }

    fun stopListeningForHabitChanges() {
        habitListenerRegistration?.remove()
        habitCompletionListenerRegistration?.remove() // Stop the new listener as well
        habitListenerRegistration = null
        habitCompletionListenerRegistration = null
        currentUserId = null
    }
}