package com.example.habits.data.habitcategories.sync

import android.util.Log
import com.example.habits.data.habitcategories.remotedatasource.CategoriesRemoteDataSource
import com.example.habits.data.habitscategory.localdatasource.CategoryLocalDataSource
import com.example.habits.data.model.habitcategory.toHabitCategoryEntity
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategorySyncer @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource,
    private val localDataSource: CategoryLocalDataSource,
    private val externalScope: CoroutineScope
) {

    private var categoryListenerRegistration: ListenerRegistration? = null
    private val _initialSyncComplete =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val initialSyncComplete = _initialSyncComplete.asSharedFlow()

    private var currentUserId: String? = null
    private val initialSyncDoneForCurrentUser = AtomicBoolean(false)


    fun startListening(userId: String) {
        if (currentUserId == userId && categoryListenerRegistration != null) {
            Log.d("CategorySyncer", "Already listening for user $userId.")
            return
        }
        stopListening()
        currentUserId = userId
        initialSyncDoneForCurrentUser.set(false)

        categoryListenerRegistration = remoteDataSource.listenToRemoteCategories(
            userId = userId,
            onDataChanged = { firestoreCategories ->
                externalScope.launch {
                    try {
                        val habitCategoryEntities = firestoreCategories.map { firestoreCategory ->
                            firestoreCategory.toHabitCategoryEntity()
                        }
                        localDataSource.replaceAllCategoriesForUser(userId, habitCategoryEntities)

                        if (!initialSyncDoneForCurrentUser.getAndSet(true)) {
                            _initialSyncComplete.tryEmit(Unit)
                        }
                    } catch (e: Exception) {
                        Log.e(
                            "CategorySyncer",
                            "Error during category sync for user $userId: ${e.message}",
                            e
                        )
                    }
                }
            },
            onError = { exception ->
                Log.e(
                    "CategorySyncer",
                    "Error listening to category changes for user $userId",
                    exception
                )
            }
        )
    }

    fun stopListening() {
        if (categoryListenerRegistration != null) {
            categoryListenerRegistration?.remove()
            categoryListenerRegistration = null
        }
        currentUserId = null
    }
}
