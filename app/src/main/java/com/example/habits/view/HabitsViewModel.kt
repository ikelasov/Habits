package com.example.habits.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.R
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay
import com.example.habits.data.repository.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitsRepository: HabitsRepository,
) : ViewModel() {

    private val _habits: MutableStateFlow<List<HabitEntity>> = MutableStateFlow(listOf())
    val habits: StateFlow<List<HabitEntity>>
        get() = _habits.asStateFlow()

    private val _viewState = MutableStateFlow(HabitsViewState(loading = true))
    val viewState: StateFlow<HabitsViewState>
        get() = _viewState

    init {
        viewModelScope.launch {
            habitsRepository.getHabits().map { habitsList ->
                return@map habitsList.map { habit ->
                    habit.mapHabitEntityToHabitUI()
                }
            }.collectLatest { habitsUi ->
                _viewState.update { it.copy(habits = habitsUi, loading = false) }
            }
        }
    }

    private fun HabitEntity.mapHabitEntityToHabitUI(): HabitUi {
        val timeToDoIndication = when (this.timeOfTheDay) {
            TimeOfTheDay.MORNING -> "9:00 AM"
            TimeOfTheDay.NOUN -> "12:00 PM"
            TimeOfTheDay.EVENING -> "9:00 PM"
            TimeOfTheDay.ALL_DAY -> "All day"
        }

        val repetitionIndication = "${this.repetitionsPerDay.toInt()} times per day"
        val progress = this.completedRepetitions / this.repetitionsPerDay
        val priorityIndicationColor = when (this.priorityLevel) {
            HabitPriorityLevel.TOP_PRIORITY -> R.color.top_priority
            HabitPriorityLevel.HIGH_PRIORITY -> R.color.high_priority
            HabitPriorityLevel.MEDIUM_PRIORITY -> R.color.medium_priority
            HabitPriorityLevel.LOW_PRIORITY -> R.color.low_priority
        }

        return HabitUi(
            id = this.id,
            name = this.name,
            timeToDoIndication = timeToDoIndication,
            repetitionIndication = repetitionIndication,
            progress = progress,
            priorityIndicationColor = priorityIndicationColor
        )
    }

    fun addHabit() {
        viewModelScope.launch {
            habitsRepository.addHabit()
        }
    }

    fun deleteHabits() {
        viewModelScope.launch {
            habitsRepository.deleteHabits()
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            habitsRepository.deleteHabit(habitId)
        }
    }
}

data class HabitsViewState(
    val habits: List<HabitUi> = listOf(),
    val loading: Boolean = false
)

data class HabitUi(
    val id: Int,
    val name: String,
    val timeToDoIndication: String,
    val repetitionIndication: String,
    val progress: Float,
    val priorityIndicationColor: Int
)