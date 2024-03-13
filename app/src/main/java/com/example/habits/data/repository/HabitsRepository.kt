package com.example.habits.data.repository

import com.example.habits.data.localdatasource.habits.HabitDao
import com.example.habits.data.localdatasource.habits.HabitEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsRepository
    @Inject
    constructor(
        private val habitDao: HabitDao,
    ) {
        fun getHabitsFlow(): Flow<List<HabitEntity>> {
            return habitDao.getHabitsFlow()
        }

        suspend fun getHabitsWithoutRemindersSet(): List<HabitEntity> {
            return habitDao.getHabitsWithoutRemindersSet()
        }

        suspend fun getHabit(habitId: Int): HabitEntity {
            return habitDao.getHabit(habitId)
        }

        suspend fun createHabit(habitEntity: HabitEntity) {
            habitDao.createHabit(habitEntity)
        }

        suspend fun deleteHabits() {
            habitDao.deleteAllHabits()
        }

        suspend fun deleteHabit(habitId: Int) {
            habitDao.deleteHabit(habitId)
        }

        suspend fun updateHabit(updatedHabitEntity: HabitEntity) {
            habitDao.updateHabit(updatedHabitEntity)
        }
    }
