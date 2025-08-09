package com.example.habits.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FirestoreCategory(
    val id: String = "",
    val name: String = "",
    val color: String = "",
    val isDefault: Boolean = false,
    val userId: String = "",
    @ServerTimestamp
    val createdAt: Date? = null
)
