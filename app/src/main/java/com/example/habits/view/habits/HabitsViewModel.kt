package com.example.habits.view.habits

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.repository.HabitsRepository
import com.example.habits.data.repository.StatisticsRepository
import com.example.habits.view.habits.mapper.mapHabitEntityListToHabitUIList
import com.example.habits.view.habits.mapper.mapToStatisticsDataUi
import com.example.habits.view.habits.utils.formatMonthYear
import com.example.habits.view.habits.utils.getDaysOfMonthAbbreviated
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel
    @Inject
    constructor(
        private val habitsRepository: HabitsRepository,
        private val statisticsRepository: StatisticsRepository,
    ) : ViewModel() {
        private val selectedMonth: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
        private val selectedDay: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())

        private val _viewState = MutableStateFlow(HabitsViewState(loading = true))
        val viewState: StateFlow<HabitsViewState>
            get() = _viewState

        init {
            viewModelScope.launch {
                combine(
                    habitsRepository.getHabits(),
                    statisticsRepository.getStatistics(),
                    selectedMonth,
                    selectedDay,
                ) { habits, statistics, selectedDate, selectedDay ->
                    val habitsUiList =
                        habits
                            .filter { it.daysToRepeat.contains(DaysOfWeek.fromLocalDate(selectedDay.dayOfWeek)) }
                            .mapHabitEntityListToHabitUIList()
                    val statisticsUi = statistics.mapToStatisticsDataUi()
                    val calendarItemsUi =
                        getDaysOfMonthAbbreviated(
                            selectedDate.year,
                            selectedDate.monthValue,
                            selectedDay,
                        )
                    val calendarDataUi =
                        CalendarDataUi(
                            selectedDate.formatMonthYear(),
                            calendarItemsUi,
                        )

                    HabitsViewState(
                        habits = habitsUiList,
                        statisticsDataUi = statisticsUi,
                        calendarDataUi = calendarDataUi,
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

        // region Habits actions
        fun addHabit() {
            viewModelScope.launch {
                habitsRepository.addMockHabit()
            }
        }

        fun deleteHabits() {
            viewModelScope.launch {
                habitsRepository.deleteHabits()
            }
        }

        fun onHabitItemDragged(
            habitId: Int,
            draggedDirection: DraggedDirection,
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                when (draggedDirection) {
                    DraggedDirection.StartToEnd -> habitsRepository.updateProgress(habitId, 1)
                    DraggedDirection.EndToStart -> habitsRepository.updateProgress(habitId, -1)
                }
            }
        }

        // endregion

        // region Date picker actions

        fun onNextMonthClicked() {
            selectedMonth.value = selectedMonth.value.plusMonths(1)
        }

        fun onPreviousMonthClicked() {
            selectedMonth.value = selectedMonth.value.minusMonths(1)
        }

        fun onCurrentDateClicked() {
            val currentDate = LocalDate.now()
            selectedDay.value = currentDate
            selectedMonth.value = currentDate
        }

        fun daySelected(dayOfMonth: Int) {
            val selectedMonth = selectedMonth.value
            val adjustedDate = selectedMonth.withDayOfMonth(dayOfMonth)
            selectedDay.value = adjustedDate
        }

        // endregion
    }

// region ViewState data model
data class HabitsViewState(
    val habits: List<HabitUi> = listOf(),
    val statisticsDataUi: StatisticsDataUi = StatisticsDataUi(),
    val calendarDataUi: CalendarDataUi = CalendarDataUi(),
    val loading: Boolean = false,
)

// endregion

// region HabitUi data model

@Stable
data class HabitUi(
    val id: Int,
    val name: String,
    val timeToDoIndication: String,
    val daysToRepeat: String,
    val repetitionIndication: String,
    val progress: Float,
    val priorityIndicationColor: Int,
)

// endregion

// region Statistics data model

@Stable
data class StatisticsDataUi(
    val longestStreak: StatisticsItemUi = StatisticsItemUi(),
    val currentStreak: StatisticsItemUi = StatisticsItemUi(),
    val completionRate: StatisticsItemUi = StatisticsItemUi(),
    val averageTasks: StatisticsItemUi = StatisticsItemUi(),
)

@Stable
data class StatisticsItemUi(
    val title: String = "",
    val hint: String = "",
    @DrawableRes val icon: Int = 0,
)

// endregion

// region Calendar data model
@Stable
data class CalendarDataUi(
    val selectedMonth: String = "",
    val daysOfMonth: List<CalendarItemUi> = listOf(),
)

@Stable
data class CalendarItemUi(
    val dayOfWeekIndication: String = "",
    val dayInMonthIndication: Int = 0,
    val isSelected: Boolean = false,
)

// endregion

// Enums

enum class DraggedDirection {
    StartToEnd,
    EndToStart,
}

// endregion
