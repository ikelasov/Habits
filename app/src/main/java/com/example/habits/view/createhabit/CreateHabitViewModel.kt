package com.example.habits.view.createhabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.R
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.domain.HabitsUseCase
import com.example.habits.exception.CreateHabitMissingFieldsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateHabitViewModel
    @Inject
    constructor(
        private val habitsUseCase: HabitsUseCase,
    ) : ViewModel() {
        private val _viewState = MutableStateFlow(ViewState())
        val viewState: StateFlow<ViewState>
            get() = _viewState

        fun attemptCreateHabit() {
            viewModelScope.launch {
                try {
                    with(_viewState.value) {
                        habitsUseCase.createHabit(
                            habitName,
                            daysToRepeat,
                            repetitionsPerDay,
                            priorityLevel,
                        )
                    }
                    _viewState.update { it.copy(habitCreated = true) }
                } catch (missingFieldsException: CreateHabitMissingFieldsException) {
                    _viewState.update { it.copy(errorMessage = R.string.create_habit_missing_fields_error) }
                }
            }
        }

        fun onSnackbarDismissed() {
            _viewState.update { it.copy(errorMessage = null) }
        }

        fun onHabitNameChanged(newName: String) {
            _viewState.update {
                it.copy(habitName = newName)
            }
        }

        fun onDaysToRepeatChanged(
            dayToRepeat: DaysOfWeek,
            isChecked: Boolean,
        ) {
            val updatedList = _viewState.value.daysToRepeat.toMutableList()
            if (isChecked) {
                updatedList.add(dayToRepeat)
            } else {
                updatedList.remove(dayToRepeat)
            }

            _viewState.update { it.copy(daysToRepeat = updatedList) }
        }

        fun onRepetitionsNumberPerDayChanged(newRepetitionsPerDay: Int) {
            _viewState.update { it.copy(repetitionsPerDay = newRepetitionsPerDay) }
        }

        fun onPriorityLevelChanged(newPriorityLevel: HabitPriorityLevel) {
            _viewState.update { it.copy(priorityLevel = newPriorityLevel) }
        }
    }

data class ViewState(
    val habitName: String = "",
    val daysToRepeat: List<DaysOfWeek> = listOf(),
    val repetitionsPerDay: Int = 1,
    val priorityLevel: HabitPriorityLevel = HabitPriorityLevel.TOP_PRIORITY,
    val errorMessage: Int? = null,
    val habitCreated: Boolean = false,
)
