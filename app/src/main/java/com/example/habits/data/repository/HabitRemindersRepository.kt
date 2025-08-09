package com.example.habits.data.repository

import com.example.habits.data.model.habits.HabitEntity
import com.example.habits.data.habits.repository.HabitRepository
import com.example.habits.worker.WorkerStarter
import javax.inject.Inject

class HabitRemindersRepository
    @Inject
    constructor(
        private val workerStarter: WorkerStarter,
        private val habitRepository: HabitRepository,
    ) {
        suspend fun createNonSetReminders() {
            val habitsWithoutReminderSet = habitRepository.getHabitsWithoutRemindersSet()
            habitsWithoutReminderSet.forEach {
                workerStarter.startWork(it)
                markHabitReminderAsSet(it)
            }
        }

        private suspend fun markHabitReminderAsSet(habit: HabitEntity) {
            val updatedHabit = habit.copy(hasSetReminder = true)
            habitRepository.updateHabit(updatedHabit)
        }
    }
