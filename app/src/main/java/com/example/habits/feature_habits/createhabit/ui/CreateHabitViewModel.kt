package com.example.habits.feature_habits.createhabit.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.HABIT_ID
import com.example.habits.R
import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.common.utils.toLocalTime
import com.example.habits.core.model.habitcategory.HabitCategoryEntity
import com.example.habits.core.model.habits.HabitPriorityLevel
import com.example.habits.feature_habits.common.domain.GetCategoriesFlowUseCase
import com.example.habits.feature_habits.createhabit.domain.CreateCategoryUseCase
import com.example.habits.feature_habits.createhabit.domain.CreateHabitUseCase
import com.example.habits.feature_habits.createhabit.domain.GetHabitByIdUseCase
import com.example.habits.feature_habits.createhabit.domain.UpdateHabitUseCase
import com.example.habits.feature_habits.createhabit.exception.CreateHabitMissingFieldsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateHabitViewModel @Inject constructor(
    private val createHabitUseCase: CreateHabitUseCase,
    private val getCategoriesFlowUseCase: GetCategoriesFlowUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState>
        get() = _viewState

    init {
        loadCategories()
        val habitId = savedStateHandle.get<String>(HABIT_ID)
        if (habitId != null) {
            loadHabitData(habitId)
        }
    }

    private fun loadHabitData(habitId: String) {
        viewModelScope.launch {
            val habit = getHabitByIdUseCase(habitId)
            val categories = getCategoriesFlowUseCase().first()
            _viewState.update {
                it.copy(
                    habitId = habitId,
                    habitName = habit.name,
                    daysToRepeat = habit.daysToRepeat,
                    repetitionsPerDay = habit.repetitionsPerDay,
                    priorityLevel = habit.priorityLevel,
                    habitExecutionTime = habit.reminderTimes.takeIf { reminderTimes -> reminderTimes.isNotEmpty() }
                        ?.first(),
                    selectedCategory = categories.find { category -> category.id == habit.categoryId },
                    isEditMode = true
                )
            }
        }
    }

    fun attemptCreateHabit() {
        viewModelScope.launch {
            try {
                with(_viewState.value) {
                    if (isEditMode) {
                        updateHabitUseCase(
                            habitId!!,
                            habitName,
                            selectedCategory?.id,
                            daysToRepeat,
                            repetitionsPerDay,
                            priorityLevel,
                            habitExecutionTime,
                        )
                    } else {
                        createHabitUseCase.createHabit(
                            habitName,
                            selectedCategory?.id,
                            daysToRepeat,
                            repetitionsPerDay,
                            priorityLevel,
                            habitExecutionTime,
                        )
                    }
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

    fun createCategory(name: String, color: Color) = viewModelScope.launch {
        val hexColor = String.format("#%06X", (0xFFFFFF and color.toArgb()))
        createCategoryUseCase(name, hexColor)
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
            getCategoriesFlowUseCase().collect { categories ->
                _viewState.update { it.copy(allCategories = categories) }
            }
        }
    }
}

data class ViewState(
    val habitId: String? = null,
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
    val selectedCategory: HabitCategoryEntity? = null,
    val isEditMode: Boolean = false,
)
