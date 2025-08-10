package com.example.habits.core.data.habitcategories.repository

import android.util.Log
import com.example.habits.core.model.habitcategory.HabitCategoryEntity
import com.example.habits.core.data.habitcategories.localdatasource.CategoryLocalDataSource
import com.example.habits.core.data.habitcategories.remotedatasource.CategoriesRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource,
    private val localDataSource: CategoryLocalDataSource,
    firebaseAuth: FirebaseAuth
) {

    private val userId by lazy { firebaseAuth.currentUser?.uid!! }

    fun getCategoriesForUserFlow(): Flow<List<HabitCategoryEntity>> =
        localDataSource.getCategoriesForUserFlow(userId)

    suspend fun createCategory(
        categoryName: String,
        colorHex: String
    ): Result<Unit> {
        return try {
            val newCategoryRef = remoteDataSource.getNewCategoryReferenceId(userId)
            remoteDataSource.createCategory(
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
            remoteDataSource.createDefaultCategoriesForUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error creating default categories for user $userId", e)
            Result.failure(e)
        }
    }
}
