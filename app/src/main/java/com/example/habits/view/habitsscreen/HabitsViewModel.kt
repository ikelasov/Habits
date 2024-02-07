package com.example.habits.view.habitsscreen

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.repository.HabitsRepository
import com.example.habits.data.repository.StatisticsRepository
import com.example.habits.view.habitsscreen.mapper.mapHabitEntityToHabitUI
import com.example.habits.view.habitsscreen.mapper.mapToStatisticsDataUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel
    @Inject
    constructor(
        private val habitsRepository: HabitsRepository,
        private val statisticsRepository: StatisticsRepository,
    ) : ViewModel() {
        private val _habits: MutableStateFlow<List<HabitEntity>> = MutableStateFlow(listOf())
        val habits: StateFlow<List<HabitEntity>>
            get() = _habits.asStateFlow()

        private val _viewState = MutableStateFlow(HabitsViewState(loading = true))
        val viewState: StateFlow<HabitsViewState>
            get() = _viewState

        init {
            viewModelScope.launch {
                combine(
                    habitsRepository.getHabits(),
                    statisticsRepository.getStatistics(),
                ) { habits, statistics ->
                    val habitsUiList = habits.map { it.mapHabitEntityToHabitUI() }
                    val statisticsUi = statistics.mapToStatisticsDataUi()

                    HabitsViewState(
                        habits = habitsUiList,
                        statisticsDataUi = statisticsUi,
                        loading = false,
                    )
                }.catch { throwable ->
                    // TODO: Implement emitting UI error. For now just rethrow
                    throw throwable
                }.collectLatest {
                    _viewState.value = it
                }
            }
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
    val statisticsDataUi: StatisticsDataUi = StatisticsDataUi(),
    val loading: Boolean = false,
)

data class HabitUi(
    val id: Int,
    val name: String,
    val timeToDoIndication: String,
    val repetitionIndication: String,
    val progress: Float,
    val priorityIndicationColor: Int,
)

data class StatisticsDataUi(
    val longestStreak: StatisticsItemUi = StatisticsItemUi(),
    val currentStreak: StatisticsItemUi = StatisticsItemUi(),
    val completionRate: StatisticsItemUi = StatisticsItemUi(),
    val averageTasks: StatisticsItemUi = StatisticsItemUi(),
)

data class StatisticsItemUi(
    val title: String = "",
    val hint: String = "",
    @DrawableRes val icon: Int = 0,
)
