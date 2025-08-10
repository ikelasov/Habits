package com.example.habits.core.model.habits

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

data class FirestoreHabitCompletion(
    val userId: String = "",
    val habitId: String = "",
    val completedRepetitions: Int = 0,
    @ServerTimestamp
    val date: Date? = null
)

fun DocumentSnapshot.toHabitCompletionEntity(): HabitCompletionEntity? {
    // Get the data object from the document
    val firestoreCompletion = this.toObject(FirestoreHabitCompletion::class.java) ?: return null

    // Parse the date directly from the document ID
    val date = try {
        LocalDate.parse(this.id, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: Exception) {
        // Handle cases where the ID might not be a valid date
        // You could log an error or return null
        return null
    }

    return HabitCompletionEntity(
        habitId = firestoreCompletion.habitId,
        date = date,
        completedRepetitions = firestoreCompletion.completedRepetitions,
        userId = firestoreCompletion.userId
    )
}
