package com.example.habits.domain

import com.example.habits.common.generateMockHabit
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay
import com.example.habits.data.repository.HabitRemindersRepository
import com.example.habits.data.repository.HabitsRepository
import com.example.habits.exception.CreateHabitMissingFields
import com.example.habits.exception.CreateHabitMissingFieldsException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitsUseCase
    @Inject
    constructor(
        private val habitsRepository: HabitsRepository,
        private val habitRemindersRepository: HabitRemindersRepository,
    ) {
        fun getHabitsFlow(): Flow<List<HabitEntity>> {
            return habitsRepository.getHabitsFlow()
        }

        suspend fun createHabit(
            habitName: String,
            daysToRepeat: List<DaysOfWeek>,
            repetitionsPerDay: Int,
            priorityLevel: HabitPriorityLevel,
        ) {
            throwIfMissingFields(habitName, daysToRepeat)

            val habitEntity =
                HabitEntity(
                    name = habitName,
                    timeOfTheDay = TimeOfTheDay.ALL_DAY,
                    repetitionsPerDay = repetitionsPerDay,
                    daysToRepeat = daysToRepeat,
                    completedRepetitions = 0,
                    priorityLevel = priorityLevel,
                    reminderTimes = emptyList(),
                )

            habitsRepository.createHabit(habitEntity)
            habitRemindersRepository.createNonSetReminders()
        }

        suspend fun addMockHabit() {
            val mockHabit = generateMockHabit()
            habitsRepository.createHabit(mockHabit)

            habitRemindersRepository.createNonSetReminders()
        }

        suspend fun deleteHabits() {
            habitsRepository.deleteHabits()
        }

        suspend fun updateProgress(
            habitId: Int,
            progressUpdateValue: Int,
        ) {
            val habit = habitsRepository.getHabit(habitId)
            if (habit.completedRepetitions == 0 && progressUpdateValue < 0) {
                return
            }
            val updatedProgress = habit.completedRepetitions + progressUpdateValue
            if (updatedProgress > habit.repetitionsPerDay) {
                return
            }
            val updatedEntity = habit.copy(completedRepetitions = updatedProgress)
            habitsRepository.updateHabit(updatedEntity)
        }

        private fun throwIfMissingFields(
            habitName: String,
            daysToRepeat: List<DaysOfWeek>,
        ) {
            val missingFields = mutableListOf<CreateHabitMissingFields>()
            if (habitName.isEmpty()) {
                missingFields.add(CreateHabitMissingFields.HABIT_NAME)
            }
            if (daysToRepeat.isEmpty()) {
                missingFields.add(CreateHabitMissingFields.DAYS_TO_REPEAT)
            }

            if (missingFields.isNotEmpty()) {
                throw CreateHabitMissingFieldsException(missingFields)
            }
        }
    }
