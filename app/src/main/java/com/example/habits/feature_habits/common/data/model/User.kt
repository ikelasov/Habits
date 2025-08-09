package com.example.habits.feature_habits.common.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    @ServerTimestamp
    val createdAt: Date? = null
)
