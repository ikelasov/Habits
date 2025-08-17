package com.example.habits.core.data.habits.remotedatasource

import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.model.habits.FirestoreHabit
import com.example.habits.core.model.habits.HabitPriorityLevel
import com.example.habits.core.model.habits.TimeOfTheDay
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.emptyList

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

    fun getNewHabitDocumentId(userId: String): String {
        val habitsCollection = firestore
            .collection("users")
            .document(userId)
            .collection("habits")
        val newHabitRef = habitsCollection.document()
        return newHabitRef.id
    }

    suspend fun createHabit(
        habitId: String,
        userId: String,
        habitName: String,
        categoryId: String,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {

        val habitsCollection = firestore
            .collection("users")
            .document(userId)
            .collection("habits")
        val habitRef = habitsCollection.document(habitId)

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

        habitRef.set(firestoreHabit).await()
    }

    suspend fun updateHabit(
        habitId: String,
        userId: String,
        habitName: String,
        categoryId: String,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?,
        hasSetReminder: Boolean
    ) {
        val habitDocRef = getHabitDocumentReference(userId, habitId)
        val firestoreUpdateData = hashMapOf<String, Any>(
            "name" to habitName,
            "categoryId" to categoryId,
            "daysToRepeat" to daysToRepeat.map { it.value },
            "repetitionsPerDay" to repetitionsPerDay,
            "priorityLevel" to priorityLevel.value,
            ("reminderTimes" to reminderTime?.let { listOf(it.toString()) }
                ?: emptyList<String>()) as Pair<String, Any>,
            "hasSetReminder" to hasSetReminder
        )

        habitDocRef.update(firestoreUpdateData).await()
    }

    suspend fun updateHabitReminderSet(habitId: String, userId: String) {
        val habitDocRef = getHabitDocumentReference(userId, habitId)

        val firestoreUpdateData = hashMapOf<String, Any>(
            "hasSetReminder" to true,
        )

        habitDocRef.update(firestoreUpdateData).await()
    }

    suspend fun deleteHabit(userId: String, habitId: String) {
        val habitDocRef = getHabitDocumentReference(userId, habitId)
        val batch = firestore.batch()

        val habitCompletionsQuery = habitDocRef.collection("habitCompletions").get().await()

        for (document in habitCompletionsQuery.documents) {
            batch.delete(document.reference)
        }

        batch.delete(habitDocRef)
        batch.commit().await()
    }

    suspend fun updateHabitProgress(
        habitId: String,
        updatedProgress: Int,
        userId: String,
        date: LocalDate
    ) {
        val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val habitCompletionDocRef = getHabitDocumentReference(userId, habitId)
            .collection("habitCompletions")
            .document(dateString)

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
