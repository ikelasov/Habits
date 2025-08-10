package com.example.habits.core.model.habits

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FirestoreHabitCompletion(
    val habitId: String = "",
    val completedRepetitions: Int = 0,
    @ServerTimestamp
    val date: Date? = null
)
