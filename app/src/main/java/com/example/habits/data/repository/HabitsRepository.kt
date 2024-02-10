package com.example.habits.data.repository

import com.example.habits.common.generateMockHabit
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitsRepository
    @Inject
    constructor(
        private val habitDao: HabitDao,
    ) {
        fun getHabits(): Flow<List<HabitEntity>> {
            return habitDao.getHabits()
        }

        suspend fun addHabit(
            habitName: String,
            daysToRepeat: List<DaysOfWeek>,
            repetitionsPerDay: Int,
            priorityLevel: HabitPriorityLevel,
        ) {
            val habitEntity =
                HabitEntity(
                    name = habitName,
                    timeOfTheDay = TimeOfTheDay.ALL_DAY,
                    repetitionsPerDay = repetitionsPerDay,
                    daysToRepeat = daysToRepeat,
                    completedRepetitions = 0,
                    priorityLevel = priorityLevel,
                )
            habitDao.addHabit(habitEntity)
        }

        suspend fun addMockHabit() {
            val mockHabit = generateMockHabit()
            habitDao.addHabit(mockHabit)
        }

        suspend fun deleteHabits() {
            habitDao.deleteAllHabits()
        }

        suspend fun deleteHabit(habitId: Int) {
            habitDao.deleteHabit(habitId)
        }
    }
