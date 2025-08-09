package com.example.habits.view.createhabit

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.R
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habitscategory.HabitCategoryEntity
import com.example.habits.domain.HabitCategoryUseCase
import com.example.habits.domain.HabitsUseCase
import com.example.habits.exception.CreateHabitMissingFieldsException
import com.example.habits.view.common.toLocalTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateHabitViewModel @Inject constructor(
    private val habitsUseCase: HabitsUseCase,
    private val habitCategoryUseCase: HabitCategoryUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState>
        get() = _viewState

    init {
        loadCategories()
    }

    fun attemptCreateHabit() {
        viewModelScope.launch {
            try {
                with(_viewState.value) {
                    habitsUseCase.createHabit(
                        habitName,
                        selectedCategory?.id,
                        daysToRepeat,
                        repetitionsPerDay,
                        priorityLevel,
                        habitExecutionTime,
                    )
                }
                _viewState.update { it.copy(habitCreated = true) }
            } catch (missingFieldsException: CreateHabitMissingFieldsException) {
                _viewState.update { it.copy(errorMessage = R.string.create_habit_missing_fields_error) }
            } catch (exception: Exception) {
                _viewState.update { it.copy(errorMessage = R.string.error_creating_habit) }
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

    fun onCreateCategoryClicked() = _viewState.update {
        it.copy(shouldShowCreateCategoryDialog = true)
    }

    fun onNewCategoryDialogDismissed() = _viewState.update {
        it.copy(shouldShowCreateCategoryDialog = false)
    }

    fun createCategory(name: String, color: Color) {
        viewModelScope.launch {
            habitCategoryUseCase.createCategory(name, color)
        }
        onNewCategoryDialogDismissed()
    }

    fun onCategorySelected(category: HabitCategoryEntity) {
        _viewState.update { it.copy(selectedCategory = category) }
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

    fun onChooseTimeClicked() {
        _viewState.update { it.copy(shouldShowTimePicker = true) }
    }

    fun onTimeChosen(hour: Int, minute: Int) {
        _viewState.update {
            it.copy(shouldShowTimePicker = false, habitExecutionTime = toLocalTime(hour, minute))
        }
    }

    fun onDialogDismissed() {
        _viewState.update { it.copy(shouldShowTimePicker = false) }
    }

    fun onPriorityLevelChanged(newPriorityLevel: HabitPriorityLevel) {
        _viewState.update { it.copy(priorityLevel = newPriorityLevel) }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            habitCategoryUseCase.getCategoriesFlow().collect { categories ->
                _viewState.update { it.copy(allCategories = categories) }
            }
        }
    }
}

data class ViewState(
    val habitName: String = "",
    val daysToRepeat: List<DaysOfWeek> = listOf(),
    val repetitionsPerDay: Int = 1,
    val priorityLevel: HabitPriorityLevel = HabitPriorityLevel.TOP_PRIORITY,
    val shouldShowTimePicker: Boolean = false,
    val shouldShowCreateCategoryDialog: Boolean = false,
    val habitExecutionTime: LocalTime? = null,
    val errorMessage: Int? = null,
    val habitCreated: Boolean = false,
    val allCategories: List<HabitCategoryEntity> = emptyList(),
    val selectedCategory: HabitCategoryEntity? = null
)
