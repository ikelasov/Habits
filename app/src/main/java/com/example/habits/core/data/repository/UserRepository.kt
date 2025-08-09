package com.example.habits.core.data.repository

import com.example.habits.feature_habits.common.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection("users")

    suspend fun createUserDocument(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
