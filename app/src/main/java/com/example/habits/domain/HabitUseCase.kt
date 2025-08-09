package com.example.habits.domain

import com.example.habits.common.generateMockHabit
import com.example.habits.data.model.habits.DaysOfWeek
import com.example.habits.data.model.habits.HabitEntity
import com.example.habits.data.model.habits.HabitPriorityLevel
import com.example.habits.data.repository.HabitRemindersRepository
import com.example.habits.data.habits.repository.HabitRepository
import com.example.habits.exception.CreateHabitMissingFields
import com.example.habits.exception.CreateHabitMissingFieldsException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import javax.inject.Inject

class HabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val habitRemindersRepository: HabitRemindersRepository,
    private val auth: FirebaseAuth,
) {
    fun getHabitsFlow(): Flow<List<HabitEntity>> {
        return habitRepository.getHabitsFlow()
    }

    suspend fun createHabit(
        habitName: String,
        categoryId: String?,
        daysToRepeat: List<DaysOfWeek>,
        repetitionsPerDay: Int,
        priorityLevel: HabitPriorityLevel,
        reminderTime: LocalTime?
    ) {
        throwIfMissingFields(habitName, daysToRepeat, categoryId)

        habitRepository.createHabit(
            habitName = habitName,
            categoryId = categoryId!!,
            daysToRepeat = daysToRepeat,
            repetitionsPerDay = repetitionsPerDay,
            priorityLevel = priorityLevel,
            reminderTime = reminderTime
        )

        habitRemindersRepository.createNonSetReminders()
    }

    suspend fun addMockHabit() {
        // TODO: Update to associate mock habit with the current user and save to Firestore
        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("User not logged in. Cannot add mock habit.")
        val mockHabit = generateMockHabit().copy(userId = userId) // Assuming HabitEntity has userId
        // This needs to call the repository method that handles Firestore + local DB
        // For now, it will likely fail or only save locally if habitsRepository.createHabit is the new one
        // habitsRepository.createHabit(mockHabit) // This old signature won't work with new repo

        // Placeholder for new way of adding mock habit
        // For now, let's assume we'll use the main createHabit logic if generateMockHabit() provides all necessary fields
        val mockDetails =
            generateMockHabit() // generateMockHabit would need to be adapted or we map its fields
        habitRepository.createHabit(
            habitName = mockDetails.name,
            categoryId = mockDetails.categoryId,
            daysToRepeat = mockDetails.daysToRepeat,
            repetitionsPerDay = mockDetails.repetitionsPerDay,
            priorityLevel = mockDetails.priorityLevel,
            reminderTime = mockDetails.reminderTimes.firstOrNull() // Adjust if generateMockHabit structure is different
        )


        habitRemindersRepository.createNonSetReminders()
    }

    suspend fun deleteHabits() {
        // TODO: Update to delete habits for the current user from Firestore and local DB
    }

    suspend fun updateProgress(
        habitId: String,
        progressUpdateValue: Int,
    ) {
        val habit = habitRepository.getHabit(habitId)
        if (habit.completedRepetitions == 0 && progressUpdateValue < 0) {
            return
        }
        val updatedProgress = habit.completedRepetitions + progressUpdateValue
        if (updatedProgress > habit.repetitionsPerDay) {
            return
        }

        habitRepository.updateHabitProgress(habitId, updatedProgress)
    }

    private fun throwIfMissingFields(
        habitName: String,
        daysToRepeat: List<DaysOfWeek>,
        categoryId: String?,
    ) {
        val missingFields = mutableListOf<CreateHabitMissingFields>()
        if (habitName.isEmpty()) {
            missingFields.add(CreateHabitMissingFields.HABIT_NAME)
        }
        if (daysToRepeat.isEmpty()) {
            missingFields.add(CreateHabitMissingFields.DAYS_TO_REPEAT)
        }
        if (categoryId == null) {
            missingFields.add(CreateHabitMissingFields.HABIT_CATEGORY)
        }

        if (missingFields.isNotEmpty()) {
            throw CreateHabitMissingFieldsException(missingFields)
        }
    }
}
