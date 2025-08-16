package com.example.habits.feature_habits.habits

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.core.common.model.DaysOfWeek
import com.example.habits.core.model.quotes.QuoteEntity
import com.example.habits.feature_habits.common.domain.GetCategoriesFlowUseCase
import com.example.habits.feature_habits.common.domain.GetHabitsFlowUseCase
import com.example.habits.feature_habits.common.mapper.mapHabitEntityListToHabitUIList
import com.example.habits.feature_habits.common.model.HabitUi
import com.example.habits.feature_habits.habits.domain.GetHabitCompletionsForDayUseCase
import com.example.habits.feature_habits.habits.domain.GetRandomQuoteUseCase
import com.example.habits.feature_habits.habits.domain.GetUsersNameUseCase
import com.example.habits.feature_habits.habits.domain.UpdateHabitProgressUseCase
import com.example.habits.feature_habits.habits.utils.formatMonthYear
import com.example.habits.feature_habits.habits.utils.getDaysOfMonthAbbreviated
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatten
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val getHabitsFlowUseCase: GetHabitsFlowUseCase,
    private val updateHabitProgressUseCase: UpdateHabitProgressUseCase,
    private val getCategoriesFlowUseCase: GetCategoriesFlowUseCase,
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase,
    private val getUsersNameUseCase: GetUsersNameUseCase,
    private val getHabitCompletionsForUseCase: GetHabitCompletionsForDayUseCase,
) : ViewModel() {
    private val selectedMonth: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    private val selectedDay: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())

    private val _viewState = MutableStateFlow(HabitsViewState(loading = true))
    val viewState: StateFlow<HabitsViewState>
        get() = _viewState

    @OptIn(ExperimentalCoroutinesApi::class)
    val completionsFlow = selectedDay.flatMapLatest { day ->
        getHabitCompletionsForUseCase(day)
    }

    init {
        fetchRandomQuote()
        getCurrentUserName()
        viewModelScope.launch {
            combine(
                getHabitsFlowUseCase(),
                getCategoriesFlowUseCase(),
                completionsFlow,
                selectedMonth,
                selectedDay,
            ) { habits, categories, completionsForDay, selectedDate, selectedDay ->
                val completionsMap =
                    completionsForDay.associateBy({ it.habitId }, { it.completedRepetitions })

                val habitsUiList =
                    habits
                        .filter { it.daysToRepeat.contains(DaysOfWeek.fromLocalDate(selectedDay.dayOfWeek)) }
                        .mapHabitEntityListToHabitUIList(categories, completionsMap)
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

                _viewState.value.copy(
                    habits = habitsUiList,
                    calendarDataUi = calendarDataUi,
                    loading = false
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
            updateHabitProgressUseCase(habitId, valueToUpdate, selectedDay.value)
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
        val randomQuote = this@HabitsViewModel.getRandomQuoteUseCase()
        _viewState.update { it.copy(quote = randomQuote) }
    }

    private fun getCurrentUserName() = viewModelScope.launch {
        val currentUserName = getUsersNameUseCase().getOrNull() ?: return@launch
        _viewState.update { it.copy(userName = currentUserName) }
    }
}

// region ViewState data model

data class HabitsViewState(
    val habits: List<HabitUi> = listOf(),
    val userName: String = "",
    val quote: QuoteEntity? = null,
    val calendarDataUi: CalendarDataUi = CalendarDataUi(),
    val loading: Boolean = false,
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
