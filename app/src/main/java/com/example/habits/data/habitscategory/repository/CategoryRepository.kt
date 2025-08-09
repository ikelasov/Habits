package com.example.habits.data.habitscategory.repository

import android.util.Log
import androidx.core.graphics.toColorInt
import com.example.habits.data.habitscategory.localdatasource.CategoryLocalDataSource
import com.example.habits.data.habitscategory.remotedatasource.HabitsCategoryRemoteDataSource
import com.example.habits.data.model.habitcategory.HabitCategoryEntity
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val habitsCategoryRemoteDataSource: HabitsCategoryRemoteDataSource,
    private val categoryLocalDataSource: CategoryLocalDataSource,
    private val externalScope: CoroutineScope
) {

    private var categoryListenerRegistration: ListenerRegistration? = null

    fun getCategoriesForUserFlow(userId: String): Flow<List<HabitCategoryEntity>> =
        categoryLocalDataSource.getCategoriesForUserFlow(userId)

    fun startListeningForCategoryChanges(userId: String) {
        stopListeningForCategoryChanges()

        categoryListenerRegistration = habitsCategoryRemoteDataSource.listenToRemoteCategories(
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
            val newCategoryRef = habitsCategoryRemoteDataSource.getNewCategoryReferenceId(userId)
            // Firestore write only; listener will handle local update
            habitsCategoryRemoteDataSource.createCategory(
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
            habitsCategoryRemoteDataSource.createDefaultCategoriesForUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error creating default categories for user $userId", e)
            Result.failure(e)
        }
    }
}