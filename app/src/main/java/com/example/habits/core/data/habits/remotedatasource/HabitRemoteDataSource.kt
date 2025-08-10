package com.example.habits.core.data.habits.remotedatasource

import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.model.habits.FirestoreHabit
import com.example.habits.core.model.habits.FirestoreHabitCompletion
import com.example.habits.core.model.habits.HabitPriorityLevel
import com.example.habits.core.model.habits.TimeOfTheDay
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import javax.inject.Inject

class HabitRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun listenToRemoteHabits(
        userId: String,
        onDataChanged: (List<FirestoreHabit>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return getHabitsCollectionReference(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val habits = snapshot.documents.mapNotNull {
                    it.toObject(FirestoreHabit::class.java)
                }
                onDataChanged(habits)
            }
        }
    }

    fun listenToAllHabitCompletions(
        userId: String,
        onDataChanged: (List<DocumentSnapshot>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        val query = firestore.collectionGroup("habitCompletions")
            .whereEqualTo("userId", userId)

        return query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                onDataChanged(snapshot.documents)
            }
        }
    }


    suspend fun createHabitAndGetDocId(
        userId: String,
        habitName: String,
        categoryId: String,
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

    suspend fun updateHabitReminderSet(habitId: String, userId: String) {
        val habitDocRef = getHabitDocumentReference(userId, habitId)

        val firestoreUpdateData = hashMapOf<String, Any>(
            "hasSetReminder" to true,
        )

        habitDocRef.update(firestoreUpdateData).await()
    }

    suspend fun deleteHabit(userId: String, habitId: String) {
        getHabitDocumentReference(userId, habitId).delete().await()
    }

    suspend fun updateHabitProgress(
        habitId: String,
        updatedProgress: Int,
        userId: String,
        date: String
    ) {
        val habitCompletionDocRef = getHabitDocumentReference(userId, habitId)
            .collection("habitCompletions")
            .document(date)

        val completionData = hashMapOf(
            "completedRepetitions" to updatedProgress,
            "habitId" to habitId,
            "date" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
            "userId" to userId
        )

        habitCompletionDocRef.set(completionData, com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    private fun getHabitsCollectionReference(userId: String) =
        firestore.collection("users")
            .document(userId)
            .collection("habits")

    private fun getHabitDocumentReference(userId: String, habitId: String) =
        firestore.collection("users")
            .document(userId)
            .collection("habits")
            .document(habitId)
}
