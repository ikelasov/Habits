package com.example.habits.data.habitscategory.remotedatasource

import com.example.habits.data.model.habitcategory.FirestoreHabitCategory
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsCategoryRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val defaultCategorySpecs = listOf(
        "Health" to "#4CAF50",
        "Fitness" to "#E91E63",
        "Productivity" to "#2196F3",
        "Finance" to "#FFC107",
        "Hobbies" to "#795548",
        "Personal Growth" to "#9C27B0"
    )

    suspend fun createCategory(
        userId: String,
        documentReference: DocumentReference,
        categoryName: String,
        colorHex: String
    ) {
        val firestoreHabitCategory = FirestoreHabitCategory(
            id = documentReference.id,
            name = categoryName,
            color = colorHex,
            isDefault = false,
            userId = userId
        )

        documentReference.set(firestoreHabitCategory).await()
    }

    suspend fun createDefaultCategoriesForUser(userId: String): List<FirestoreHabitCategory> {
        val userCategoriesCollection =
            getCategoriesCollectionReference(userId)
        val categoriesCreated =
            mutableListOf<FirestoreHabitCategory>()
        val batch = firestore.batch()

        defaultCategorySpecs.forEach { (name, color) ->
            val newCategoryRef = userCategoriesCollection.document()
            val newId = newCategoryRef.id

            val firestoreHabitCategory = FirestoreHabitCategory(
                id = newId,
                name = name,
                color = color,
                isDefault = true,
                userId = userId
            )

            batch.set(newCategoryRef, firestoreHabitCategory)
            categoriesCreated.add(firestoreHabitCategory)
        }

        batch.commit().await()

        return categoriesCreated
    }

    fun getNewCategoryReferenceId(userId: String): DocumentReference =
        firestore.collection("users")
            .document(userId)
            .collection("categories")
            .document()

    fun getCategoriesCollectionReference(userId: String): CollectionReference =
        firestore.collection("users")
            .document(userId)
            .collection("categories")
}