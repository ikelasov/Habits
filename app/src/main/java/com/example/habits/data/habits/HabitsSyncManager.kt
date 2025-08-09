package com.example.habits.data.habits

import com.example.habits.data.habits.repository.HabitRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class HabitsSyncManager @Inject constructor(
    private val habitRepository: HabitRepository
) {

    fun startListeningForHabitChanges(userId: String) {
        habitRepository.startListeningForHabitChanges(userId)
    }

    fun stopListeningForHabitChanges() {
        habitRepository.stopListeningForHabitChanges()
    }
}
