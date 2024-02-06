package com.example.habits.data.repository

import com.example.habits.common.generateMockHabit
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

    suspend fun deleteHabits() {
        habitDao.deleteAllHabits()
    }

    suspend fun deleteHabit(habitId: Int) {
        habitDao.deleteHabit(habitId)
    }

}