package com.example.habits.data.repository

import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.random.Random

class HabitsRepository @Inject constructor(
    private val habitDao: HabitDao
) {

    fun getHabits(): Flow<List<HabitEntity>> {
        return habitDao.getHabits()
    }

    suspend fun addHabit() {
        val mockHabit = generateMockHabit()
        habitDao.addHabit(mockHabit)
    }

    private fun generateMockHabit(): HabitEntity {
        val listOfNames = listOf(
            "Go to the gym",
            "Drink water",
            "Clean bedroom",
            "Meditate",
            "Eat a frog"
        )
        val habitName = listOfNames.random()
        val timeOfTheDay = TimeOfTheDay.values().random()
        val repetitionsPerDay = Random.nextInt(from = 1, until = 10)
        val completedRepetitions = Random.nextInt(from = 0, until = repetitionsPerDay)
        val habitPriorityLevel = HabitPriorityLevel.values().random()


        return HabitEntity(
            name = habitName,
            comment = "Comment",
            timeOfTheDay = timeOfTheDay,
            repetitionsPerDay = repetitionsPerDay.toFloat(),
            completedRepetitions = completedRepetitions.toFloat(),
            priorityLevel = habitPriorityLevel
        )
    }

    suspend fun deleteHabits() {
        habitDao.deleteAllHabits()
    }

    suspend fun deleteHabit(habitId: Int) {
        habitDao.deleteHabit(habitId)
    }

}