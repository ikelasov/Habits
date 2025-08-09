package com.example.habits.data.habitcategories.repository

import android.util.Log
import androidx.core.graphics.toColorInt
import com.example.habits.data.habitscategory.localdatasource.CategoryLocalDataSource
import com.example.habits.data.habitscategory.remotedatasource.CategoriesRemoteDataSource
import com.example.habits.data.model.habitcategory.HabitCategoryEntity
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoriesRemoteDataSource: CategoriesRemoteDataSource,
    private val categoryLocalDataSource: CategoryLocalDataSource,
    private val externalScope: CoroutineScope
) {

    private var categoryListenerRegistration: ListenerRegistration? = null

    private val _initialSyncComplete = MutableSharedFlow<Unit>(replay = 1)
    val initialSyncComplete = _initialSyncComplete.asSharedFlow()

    fun getCategoriesForUserFlow(userId: String): Flow<List<HabitCategoryEntity>> =
        categoryLocalDataSource.getCategoriesForUserFlow(userId)

    fun startListeningForCategoryChanges(userId: String) {
        stopListeningForCategoryChanges()

        val initialSyncDone = AtomicBoolean(false)

        categoryListenerRegistration = categoriesRemoteDataSource.listenToRemoteCategories(
            userId = userId,
            onDataChanged = { firestoreCategories ->
                externalScope.launch {
                    val habitCategoryEntities = firestoreCategories.map { firestoreCategory ->
                        HabitCategoryEntity(
                            id = firestoreCategory.id,
                            name = firestoreCategory.name,
                            color = firestoreCategory.color.toColorInt(),
                            isDefault = firestoreCategory.isDefault,
                            userId = firestoreCategory.userId,
                            createdAt = firestoreCategory.createdAt?.time
                                ?: System.currentTimeMillis()
                        )
                    }

                    categoryLocalDataSource.replaceAllCategoriesForUser(
                        userId,
                        habitCategoryEntities
                    )

                    if (!initialSyncDone.getAndSet(true)) {
                        _initialSyncComplete.tryEmit(Unit)
                    }
                }
            },
            onError = { exception ->
                Log.e(
                    "CategoryRepository",
                    "Error listening to category changes for user $userId",
                    exception
                )
            }
        )
    }

    fun stopListeningForCategoryChanges() {
        categoryListenerRegistration?.remove()
        categoryListenerRegistration = null
    }

    suspend fun createCategory(
        userId: String,
        categoryName: String,
        colorHex: String
    ): Result<Unit> {
        return try {
            val newCategoryRef = categoriesRemoteDataSource.getNewCategoryReferenceId(userId)
            // Firestore write only; listener will handle local update
            categoriesRemoteDataSource.createCategory(
                userId,
                newCategoryRef,
                categoryName,
                colorHex
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error creating category for user $userId", e)
            Result.failure(e)
        }
    }

    suspend fun createDefaultCategoriesForUser(userId: String): Result<Unit> {
        return try {
            // Firestore write only; listener will handle local update
            categoriesRemoteDataSource.createDefaultCategoriesForUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error creating default categories for user $userId", e)
            Result.failure(e)
        }
    }
}