package com.example.habits.feature_habits.manage_habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.feature_habits.manage_habits.domain.DeleteHabitUseCase
import com.example.habits.feature_habits.common.domain.GetCategoriesFlowUseCase
import com.example.habits.feature_habits.common.domain.GetHabitsFlowUseCase
import com.example.habits.feature_habits.common.mapper.mapHabitEntityListToHabitUIList
import com.example.habits.feature_habits.common.model.HabitUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    val state = combine(
        getHabitsFlowUseCase(),
        getCategoriesFlowUseCase(),
    ) { habits, categories ->
        _isLoading.update { false }
        ScreenState(habits.mapHabitEntityListToHabitUIList(categories, emptyMap()))
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
}

data class ScreenState(val habits: List<HabitUi> = emptyList())
