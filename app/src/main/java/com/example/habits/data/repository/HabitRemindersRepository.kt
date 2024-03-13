package com.example.habits.data.repository

import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.worker.WorkerStarter
import javax.inject.Inject

class HabitRemindersRepository
    @Inject
    constructor(
        private val workerStarter: WorkerStarter,
        private val habitsRepository: HabitsRepository,
    ) {
        suspend fun createNonSetReminders() {
            val habitsWithoutReminderSet = habitsRepository.getHabitsWithoutRemindersSet()
            habitsWithoutReminderSet.forEach {
                workerStarter.startWork(it)
                markHabitReminderAsSet(it)
            }
        }

        private suspend fun markHabitReminderAsSet(habit: HabitEntity) {
            val updatedHabit = habit.copy(hasSetReminder = true)
            habitsRepository.updateHabit(updatedHabit)
        }
    }
