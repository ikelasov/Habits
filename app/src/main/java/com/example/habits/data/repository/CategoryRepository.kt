package com.example.habits.data.repository

import android.util.Log
import androidx.core.graphics.toColorInt
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryDao
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryEntity
import com.example.habits.data.model.FirestoreCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val categoryDao: HabitCategoryDao
) {

    private val defaultCategorySpecs = listOf(
        "Health" to "#4CAF50",
        "Fitness" to "#E91E63",
        "Productivity" to "#2196F3",
        "Finance" to "#FFC107",
        "Hobbies" to "#795548",
        "Personal Growth" to "#9C27B0"
    )

    fun getCategoriesForUserFlow(userId: String): Flow<List<HabitCategoryEntity>> {
        return categoryDao.getCategoriesForUserFlow(userId)
    }

    suspend fun createCategory(
        userId: String,
        categoryName: String,
        colorHex: String
    ): Result<Unit> {
        return try {
            val userCategoriesCollection =
                firestore.collection("users").document(userId).collection("categories")
            val newCategoryRef = userCategoriesCollection.document()
            val categoryId = newCategoryRef.id

            val firestoreCategory = FirestoreCategory(
                id = categoryId,
                name = categoryName,
                color = colorHex,
                isDefault = false,
                userId = userId
            )

            newCategoryRef.set(firestoreCategory).await()

            categoryDao.insert(
                HabitCategoryEntity(
                    id = categoryId,
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

    /**
     * Creates the set of default categories for a new user in both Firestore and the local Room DB
     * using an atomic WriteBatch for efficiency.
     * This should be called once upon user registration.
     */
    suspend fun createDefaultCategoriesForUser(userId: String): Result<Unit> {
        return try {
            val userCategoriesCollection =
                firestore.collection("users").document(userId).collection("categories")
            val localEntities = mutableListOf<HabitCategoryEntity>()

            val batch = firestore.batch()

            for ((name, color) in defaultCategorySpecs) {
                val newCategoryRef = userCategoriesCollection.document()
                val newId = newCategoryRef.id

                val firestoreCategory = FirestoreCategory(
                    id = newId,
                    name = name,
                    color = color,
                    isDefault = true,
                    userId = userId
                )

                batch.set(newCategoryRef, firestoreCategory)

                localEntities.add(
                    HabitCategoryEntity(
                        id = newId,
                        name = name,
                        color = color.toColorInt(),
                        isDefault = true,
                        userId = userId,
                        createdAt = System.currentTimeMillis()
                    )
                )
            }

            batch.commit().await()

            if (localEntities.isNotEmpty()) {
                categoryDao.insertAll(localEntities)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error creating default categories for user $userId", e)
            Result.failure(e)
        }
    }
}
