package com.example.habits.core.data.repository

import com.example.habits.core.data.habits.repository.HabitRepository
import com.example.habits.core.model.habits.HabitEntity
import com.example.habits.core.worker.WorkerStarter
import javax.inject.Inject

class HabitRemindersRepository @Inject constructor(
    private val workerStarter: WorkerStarter,
    private val habitRepository: HabitRepository,
) {
    suspend fun createNonSetReminders() {
        val habitsWithoutReminderSet = habitRepository.getHabitsWithoutRemindersSet()
        habitsWithoutReminderSet.forEach {
            if (it.reminderTimes.isEmpty()) return
            workerStarter.startWork(it)
            markHabitReminderAsSet(it)
        }
    }

    private suspend fun markHabitReminderAsSet(habit: HabitEntity) =
        habitRepository.updateHabitRemindersSet(habit.id)
}
