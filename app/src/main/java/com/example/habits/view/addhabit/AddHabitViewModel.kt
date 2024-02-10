package com.example.habits.view.addhabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.repository.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel
    @Inject
    constructor(
        private val habitsRepository: HabitsRepository,
    ) : ViewModel() {
        private val _viewState = MutableStateFlow(ViewState())
        val viewState: StateFlow<ViewState>
            get() = _viewState

        fun attemptCreateHabit() {
            viewModelScope.launch {
                if (!allFieldsAreFilled()) {
                    _viewState.update { it.copy(errorMessage = "All fields must be filled") }
                } else {
                    with(viewState.value) {
                        habitsRepository.addHabit(
                            habitName,
                            daysToRepeat,
                            repetitionsPerDay,
                            priorityLevel,
                        )
                        _viewState.update { it.copy(habitCreated = true) }
                    }
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
            _viewState.update {
                val tempList = it.daysToRepeat.toMutableList()
                if (isChecked) {
                    tempList.add(dayToRepeat)
                } else {
                    tempList.remove(dayToRepeat)
                }
                it.copy(
                    daysToRepeat = tempList,
                )
            }
        }

        fun onRepetitionsNumberPerDayChanged(newRepetitionsPerDay: Int) {
            _viewState.update { it.copy(repetitionsPerDay = newRepetitionsPerDay) }
        }

        fun onPriorityLevelChanged(newPriorityLevel: HabitPriorityLevel) {
            _viewState.update { it.copy(priorityLevel = newPriorityLevel) }
        }

        private fun allFieldsAreFilled(): Boolean =
            with(viewState.value) {
                habitName != "" && daysToRepeat.isNotEmpty()
            }
    }

data class ViewState(
    val habitName: String = "",
    val daysToRepeat: List<DaysOfWeek> = listOf(),
    val repetitionsPerDay: Int = 1,
    val priorityLevel: HabitPriorityLevel = HabitPriorityLevel.TOP_PRIORITY,
    val errorMessage: String? = null,
    val habitCreated: Boolean = false,
)
