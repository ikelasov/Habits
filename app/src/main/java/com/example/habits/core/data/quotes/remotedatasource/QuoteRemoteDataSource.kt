package com.example.habits.core.data.quotes.remotedatasource

import com.example.habits.core.model.quotes.FireStoreQuote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuoteRemoteDataSource @Inject constructor(
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
