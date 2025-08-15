package com.example.habits.feature_habits.manage_habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.core.model.habits.HabitPriorityLevel
import com.example.habits.feature_habits.manage_habits.domain.DeleteHabitUseCase
import com.example.habits.feature_habits.common.domain.GetCategoriesFlowUseCase
import com.example.habits.feature_habits.common.domain.GetHabitsFlowUseCase
import com.example.habits.feature_habits.common.mapper.mapHabitEntityListToHabitUIList
import com.example.habits.feature_habits.common.model.HabitUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageHabitsViewModel @Inject constructor(
    private val deleteHabitUseCase: DeleteHabitUseCase,
    getHabitsFlowUseCase: GetHabitsFlowUseCase,
    getCategoriesFlowUseCase: GetCategoriesFlowUseCase
) : ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val selectedPriority = MutableStateFlow<HabitPriorityLevel?>(null)

    val state = combine(
        getHabitsFlowUseCase(),
        getCategoriesFlowUseCase(),
        selectedPriority
    ) { habits, categories, priority ->
        _isLoading.update { false }

        val mappedHabits = habits.mapHabitEntityListToHabitUIList(categories, emptyMap())

        val filteredHabits = if (priority == null) {
            mappedHabits
        } else {
            mappedHabits.filter { it.priorityLevel == priority }
        }

        ScreenState(
            habits = filteredHabits,
            selectedPriority = priority
        )
    }.catch { throwable ->
        _isLoading.update { false }
        _message.emit(throwable.message ?: "Unknown error")
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ScreenState()
    )

    fun onDeleteHabitClicked(habitId: String) {
        viewModelScope.launch {
            try {
                deleteHabitUseCase(habitId)
            } catch (e: Exception) {
                _message.emit(e.message ?: "Failed to delete habit")
            }
        }
    }

    fun onPriorityFilterChanged(priority: HabitPriorityLevel?) {
        selectedPriority.value = priority
    }
}

data class ScreenState(
    val habits: List<HabitUi> = emptyList(),
    val selectedPriority: HabitPriorityLevel? = null
)