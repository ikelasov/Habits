package com.example.habits.data.habitscategory.repository

import android.util.Log
import androidx.core.graphics.toColorInt
import com.example.habits.data.habitscategory.localdatasource.CategoryLocalDataSource
import com.example.habits.data.model.habitcategory.HabitCategoryEntity
import com.example.habits.data.habitscategory.remotedatasource.HabitsCategoryRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val habitsCategoryRemoteDataSource: HabitsCategoryRemoteDataSource,
    private val categoryLocalDataSource: CategoryLocalDataSource
) {

    fun getCategoriesForUserFlow(userId: String): Flow<List<HabitCategoryEntity>> {
        return categoryLocalDataSource.getCategoriesForUserFlow(userId)
    }

    suspend fun createCategory(
        userId: String,
        categoryName: String,
        colorHex: String
    ): Result<Unit> {
        return try {
            val newCategoryRef = habitsCategoryRemoteDataSource.getNewCategoryReferenceId(userId)
            habitsCategoryRemoteDataSource.createCategory(
                userId,
                newCategoryRef,
                categoryName,
                colorHex
            )

            categoryLocalDataSource.insert(
                HabitCategoryEntity(
                    id = newCategoryRef.id,
                    name = categoryName,
                    color = colorHex.toColorInt(),
                    isDefault = false,
                    userId = userId,
                    createdAt = System.currentTimeMillis()
                )
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error creating category for user $userId", e)
            Result.failure(e)
        }
    }

    suspend fun createDefaultCategoriesForUser(userId: String): Result<Unit> {
        return try {
            val categoriesCreated =
                habitsCategoryRemoteDataSource.createDefaultCategoriesForUser(userId)

            val categoriesToStoreLocally = categoriesCreated.map { category ->
                HabitCategoryEntity(
                    id = category.id,
                    name = category.name,
                    color = category.color.toColorInt(),
                    isDefault = category.isDefault,
                    userId = category.userId,
                    createdAt = System.currentTimeMillis()
                )
            }

            if (categoriesToStoreLocally.isNotEmpty()) {
                categoryLocalDataSource.insertAll(categoriesToStoreLocally)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error creating default categories for user $userId", e)
            Result.failure(e)
        }
    }
}