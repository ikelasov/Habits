@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.habits.view.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme
import com.example.habits.view.components.LoadingScreen
import com.example.habits.view.habits.screencomponents.HabitItem
import com.example.habits.view.habits.screencomponents.HorizontalCalendar
import com.example.habits.view.habits.screencomponents.StatisticsContent
import com.example.habits.view.habits.screencomponents.StatisticsItem
import com.example.habits.view.habits.screencomponents.TopBar
import com.example.habits.view.habits.utils.getDaysOfMonth

@Composable
fun HabitsScreen(
    onAddHabitClicked: () -> Unit,
    habitsViewModel: HabitsViewModel = hiltViewModel(),
) {
    val viewState by habitsViewModel.viewState
        .collectAsStateWithLifecycle()

    if (viewState.loading) {
        LoadingScreen()
    } else {
        ScreenContent(
            viewState.habits,
            viewState.statisticsDataUi,
            viewState.calendarDataUi,
            onAddHabitClicked,
            habitsViewModel::onNextMonthClicked,
            habitsViewModel::onPreviousMonthClicked,
            habitsViewModel::onCurrentDateClicked,
            habitsViewModel::daySelected,
            habitsViewModel::addHabit,
            habitsViewModel::deleteHabits,
            habitsViewModel::deleteHabit,
        )
    }
}

@Composable
private fun ScreenContent(
    habits: List<HabitUi>,
    statistics: StatisticsDataUi,
    calendarDataUi: CalendarDataUi,
    onAddHabitClicked: () -> Unit,
    onNextMonthClicked: () -> Unit,
    onPreviousMonthClicked: () -> Unit,
    onCurrentDateClicked: () -> Unit,
    daySelected: (Int) -> Unit,
    addHabit: () -> Unit,
    deleteHabits: () -> Unit,
    deleteHabit: (Int) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddHabitClicked() }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            TopBar()
        },
    ) { contentPadding ->
        val listState = rememberLazyListState()
        Content(
            habits,
            statistics,
            calendarDataUi,
            onNextMonthClicked,
            onPreviousMonthClicked,
            onCurrentDateClicked,
            daySelected,
            onAddHabitClicked = { addHabit() },
            onDeleteClicked = { deleteHabits() },
            onHabitClicked = { deleteHabit(it) },
            listState,
            Modifier.padding(contentPadding),
        )
    }
}

@Composable
private fun Content(
    habits: List<HabitUi>,
    statistics: StatisticsDataUi,
    calendarDataUi: CalendarDataUi,
    onNextMonthClicked: () -> Unit,
    onPreviousMonthClicked: () -> Unit,
    onCurrentDateClicked: () -> Unit,
    daySelected: (Int) -> Unit,
    onAddHabitClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onHabitClicked: (Int) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier.background(colorResource(R.color.background)),
    ) {
        item { StatisticsContent(statistics) }
        item { Spacer(modifier = Modifier.padding(vertical = 8.dp)) }
        item {
            HorizontalCalendar(
                calendarDataUi.selectedMonth,
                onNextMonthClicked,
                onPreviousMonthClicked,
                onCurrentDateClicked,
                calendarDataUi.daysOfMonth,
                daySelected,
                Modifier.padding(horizontal = 16.dp),
            )
        }
        item { Spacer(modifier = Modifier.padding(vertical = 8.dp)) }
        items(
            items = habits,
            key = { it.id },
        ) {
            HabitItem(
                habit = it,
                onHabitClicked = onHabitClicked,
            )
        }

        item {
            AddHabitButton(onButtonClick = { onAddHabitClicked() })
        }
        item {
            DeleteHabitsButton(onButtonClicked = { onDeleteClicked() })
        }
    }
}

@Composable
private fun AddHabitButton(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { onButtonClick() },
        modifier = modifier,
    ) {
        Text(text = "Add")
    }
}

@Composable
private fun DeleteHabitsButton(
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(onClick = { onButtonClicked() }, modifier = modifier) {
        Text(text = "Delete")
    }
}

// region Preview

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}

@Preview
@Composable
fun HabitItemPreview() {
    val mockHabit =
        HabitUi(
            id = 0,
            name = "Go to the gym",
            timeToDoIndication = "10:00 AM",
            daysToRepeat = "Mon,Sun",
            repetitionIndication = "10 times per day",
            0.3f,
            R.color.purple_200,
        )
    HabitsTheme {
        HabitItem(mockHabit, {})
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsItemPreview() {
    StatisticsItem(
        statisticTitle = "20 Days",
        statisticsComment = "Longest streak",
        icon = R.drawable.ic_longest_streak,
    )
}

@Preview
@Composable
fun StatisticsContentPreview() {
    StatisticsContent(getMockStatisticsDate())
}

@Preview
@Composable
fun ScreenPreview() {
    val mockHabit =
        HabitUi(
            id = 0,
            name = "Go to the gym",
            timeToDoIndication = "10:00 AM",
            daysToRepeat = "Mon,Sun",
            repetitionIndication = "10 times per day",
            0.3f,
            R.color.purple_200,
        )
    ScreenContent(
        listOf(mockHabit),
        getMockStatisticsDate(),
        CalendarDataUi(selectedMonth = "February 2024", daysOfMonth = getDaysOfMonth(2024, 2)),
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
    )
}

// endregion

// TODO move this
internal fun getMockStatisticsDate(): StatisticsDataUi {
    val longestStreak =
        StatisticsItemUi("20 Days", "Longest streak", R.drawable.ic_longest_streak)
    val currentStreak =
        StatisticsItemUi("7 Days", "Current streak", R.drawable.ic_current_streak)
    val completionRate =
        StatisticsItemUi("98%", "Completion rate", R.drawable.ic_completion_rate)
    val averageTasks = StatisticsItemUi("7", "Average tasks", R.drawable.ic_average_tasks)

    return StatisticsDataUi(
        longestStreak = longestStreak,
        currentStreak = currentStreak,
        completionRate = completionRate,
        averageTasks = averageTasks,
    )
}
