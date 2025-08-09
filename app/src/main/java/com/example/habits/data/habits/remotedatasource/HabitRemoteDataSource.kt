package com.example.habits.data.habits.remotedatasource

import com.example.habits.data.model.habits.DaysOfWeek
import com.example.habits.data.model.habits.HabitPriorityLevel
import com.example.habits.data.model.habits.TimeOfTheDay
import com.example.habits.data.model.habits.FirestoreHabit
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createHabitAndGetDocId(
        userId: String,
        habitName: String,
        categoryId: String?,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ): String {

        val habitsCollection = firestore
            .collection("users")
            .document(userId)
            .collection("habits")
        val newHabitRef = habitsCollection.document()
        val habitId = newHabitRef.id

        val firestoreHabit = FirestoreHabit(
            id = habitId,
            userId = userId,
            categoryId = categoryId,
            name = habitName,
            timeOfTheDay = TimeOfTheDay.ALL_DAY.value,
            daysToRepeat = daysToRepeat.map { it.value },
            repetitionsPerDay = repetitionsPerDay,
            completedRepetitions = 0,
            priorityLevel = priorityLevel.value,
            hasSetReminder = false,
            reminderTimes = reminderTime?.let { listOf(it.toString()) } ?: emptyList(),
        )

        newHabitRef.set(firestoreHabit).await()

        return habitId
    }

    suspend fun updateHabitProgress(habitId: String, updatedProgress: Int, userId: String) {
        val habitDocRef = firestore.collection("users").document(userId)
            .collection("habits").document(habitId)

        val firestoreUpdateData = hashMapOf<String, Any>(
            "completedRepetitions" to updatedProgress,
        )

        habitDocRef.update(firestoreUpdateData).await()
    }

}