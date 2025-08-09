package com.example.habits.view.habits

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.data.habits.localdatasource.DaysOfWeek
import com.example.habits.data.quotes.localdatasource.QuoteEntity
import com.example.habits.data.repository.StatisticsRepository
import com.example.habits.domain.HabitCategoryUseCase
import com.example.habits.domain.HabitsUseCase
import com.example.habits.domain.QuoteUseCase
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel
@Inject
constructor(
    private val habitsUseCase: HabitsUseCase,
    private val categoryUseCase: HabitCategoryUseCase,
    private val quoteUseCase: QuoteUseCase,
    private val statisticsRepository: StatisticsRepository,
) : ViewModel() {
    private val selectedMonth: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    private val selectedDay: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())

    private val _viewState = MutableStateFlow(HabitsViewState(loading = true))
    val viewState: StateFlow<HabitsViewState>
        get() = _viewState

    init {
        fetchRandomQuote()
        viewModelScope.launch {
            combine(
                habitsUseCase.getHabitsFlow(),
                categoryUseCase.getCategoriesFlow(),
                statisticsRepository.getStatistics(),
                selectedMonth,
                selectedDay,
            ) { habits, categories, statistics, selectedDate, selectedDay ->
                val habitsUiList =
                    habits
                        .filter { it.daysToRepeat.contains(DaysOfWeek.fromLocalDate(selectedDay.dayOfWeek)) }
                        .mapHabitEntityListToHabitUIList(categories)
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
                    quote = _viewState.value.quote
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
    fun addMockHabit() {
        viewModelScope.launch {
            habitsUseCase.addMockHabit()
        }
    }

    fun deleteHabits() {
        viewModelScope.launch {
            habitsUseCase.deleteHabits()
        }
    }

    fun onHabitItemDragged(
        habitId: String,
        draggedDirection: DraggedDirection,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val valueToUpdate =
                when (draggedDirection) {
                    DraggedDirection.StartToEnd -> 1
                    DraggedDirection.EndToStart -> -1
                }
            habitsUseCase.updateProgress(habitId, valueToUpdate)
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

    fun onDayClicked(dayOfMonth: Int) {
        val selectedMonth = selectedMonth.value
        val adjustedDate = selectedMonth.withDayOfMonth(dayOfMonth)
        selectedDay.value = adjustedDate
    }

    // endregion

    private fun fetchRandomQuote() = viewModelScope.launch {
        val randomQuote = quoteUseCase.getRandomQuote()
        _viewState.update { it.copy(quote = randomQuote) }
    }
}

// region ViewState data model
data class HabitsViewState(
    val habits: List<HabitUi> = listOf(),
    val quote: QuoteEntity? = null,
    val statisticsDataUi: StatisticsDataUi = StatisticsDataUi(),
    val calendarDataUi: CalendarDataUi = CalendarDataUi(),
    val loading: Boolean = false,
)

// endregion

// region HabitUi data model

@Stable
data class HabitUi(
    val id: String,
    val name: String,
    val category: String,
    val categoryColor: Color?,
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

// region Enums

enum class DraggedDirection {
    StartToEnd,
    EndToStart,
}

// endregion
