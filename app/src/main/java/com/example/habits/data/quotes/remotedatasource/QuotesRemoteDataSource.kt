package com.example.habits.data.quotes.remotedatasource

import com.example.habits.data.model.FireStoreQuote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuotesRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getQuotes(): List<FireStoreQuote> {
        return try {
            firestore.collection("quotes")
                .get()
                .await()
                .toObjects(FireStoreQuote::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
