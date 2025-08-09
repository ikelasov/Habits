package com.example.habits.data.repository

import android.util.Log
import androidx.compose.animation.core.copy
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay
import com.example.habits.data.localdatasource.quotes.QuoteDao
import com.example.habits.data.localdatasource.quotes.QuoteEntity
import com.example.habits.data.model.FirestoreHabit
import com.example.habits.data.model.toHabitEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val quoteDao: QuoteDao
) {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun getHabitsFlow(): Flow<List<HabitEntity>> {
        return habitDao.getHabitsFlow()
    }

    suspend fun getRandomQuote(): QuoteEntity? {
        return quoteDao.getRandomQuote()
    }

    suspend fun getHabitsWithoutRemindersSet(): List<HabitEntity> {
        return habitDao.getHabitsWithoutRemindersSet()
    }

    suspend fun getHabit(habitId: String): HabitEntity {
        return habitDao.getHabit(habitId)
    }

    suspend fun createHabit(
        userId: String,
        habitName: String,
        categoryId: String?,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {
        val habitsCollection = firestore.collection("users").document(userId).collection("habits")
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
            reminderTimes = reminderTime?.let { listOf(it.format(timeFormatter)) } ?: emptyList(),
        )

        newHabitRef.set(firestoreHabit).await()

        habitDao.createHabit(firestoreHabit.toHabitEntity())
    }

    suspend fun deleteHabits() {
        habitDao.deleteAllHabits()
    }

    suspend fun deleteHabit(habitId: String) {
        habitDao.deleteHabit(habitId)
    }

    suspend fun updateHabitProgress(habitId: String, updatedProgress: Int) {
        // TODO handle exception
        val currentUserId = firebaseAuth.currentUser?.uid
            ?: throw Exception()

        try {
            val habitDocRef = firestore.collection("users").document(currentUserId)
                .collection("habits").document(habitId)

            val firestoreUpdateData = hashMapOf<String, Any>(
                "completedRepetitions" to updatedProgress,
            )

            habitDocRef.update(firestoreUpdateData).await()

            val existingHabitEntity = habitDao.getHabit(habitId)
            val updatedHabitEntity = existingHabitEntity.copy(
                completedRepetitions = updatedProgress
            )
            habitDao.updateHabit(updatedHabitEntity)
        } catch (e: Exception) {
            throw Exception()
        }
    }

    suspend fun updateHabit(updatedHabitEntity: HabitEntity) {
        habitDao.updateHabit(updatedHabitEntity)
    }
}
