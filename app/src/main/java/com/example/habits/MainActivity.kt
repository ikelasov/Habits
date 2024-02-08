@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.habits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habits.ui.theme.HabitsTheme
import com.example.habits.view.habitsscreen.CalendarDataUi
import com.example.habits.view.habitsscreen.HabitUi
import com.example.habits.view.habitsscreen.HabitsViewModel
import com.example.habits.view.habitsscreen.HorizontalCalendar
import com.example.habits.view.habitsscreen.StatisticsDataUi
import com.example.habits.view.habitsscreen.StatisticsItemUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitsTheme {
                val habitsViewModel: HabitsViewModel = viewModel()
                val viewState by habitsViewModel.viewState
                    .collectAsStateWithLifecycle()

                if (viewState.loading) {
                    LoadingScreen()
                } else {
                    HabitsScreen(
                        viewState.habits,
                        viewState.statisticsDataUi,
                        viewState.calendarDataUi,
                        habitsViewModel::onNextMonthClicked,
                        habitsViewModel::onPreviousMonthClicked,
                        habitsViewModel::onCurrentDateClicked,
                        habitsViewModel::daySelected,
                        habitsViewModel::addHabit,
                        habitsViewModel::deleteHabit,
                        habitsViewModel::deleteHabits,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp))
    }
}

@ExperimentalMaterial3Api
@Composable
private fun HabitsScreen(
    habits: List<HabitUi>,
    statistics: StatisticsDataUi,
    calendarDataUi: CalendarDataUi,
    onNextMonthClicked: () -> Unit,
    onPreviousMonthClicked: () -> Unit,
    onCurrentDateClicked: () -> Unit,
    daySelected: (Int) -> Unit,
    addHabit: () -> Unit,
    deleteHabit: (Int) -> Unit,
    deleteHabits: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            TopBar()
        },
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            ScreenContent(
                habits,
                statistics,
                calendarDataUi,
                onNextMonthClicked,
                onPreviousMonthClicked,
                onCurrentDateClicked,
                daySelected,
                addHabit,
                deleteHabits,
                deleteHabit,
            )
        }
    }
}

@Composable
fun TopBar(
    userName: String = "Ilias",
    @DrawableRes profileIcon: Int = R.drawable.ic_profile,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .padding(24.dp)
                .fillMaxWidth(),
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Text(text = "Hello, ")
            Text(text = "$userName!", color = colorResource(R.color.medium_priority))
        }
        Image(
            painter = painterResource(profileIcon),
            contentDescription = null,
            modifier = Modifier.size(34.dp),
        )
    }
}

@Composable
fun StatisticsContent(statistics: StatisticsDataUi) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(0.5f)) {
                StatisticsItem(
                    statisticTitle = statistics.longestStreak.title,
                    statisticsComment = statistics.longestStreak.hint,
                    icon = statistics.longestStreak.icon,
                )
                StatisticsItem(
                    statisticTitle = statistics.completionRate.title,
                    statisticsComment = statistics.completionRate.hint,
                    icon = statistics.completionRate.icon,
                )
            }
            Column(modifier = Modifier.weight(0.5f)) {
                StatisticsItem(
                    statisticTitle = statistics.currentStreak.title,
                    statisticsComment = statistics.currentStreak.hint,
                    icon = statistics.currentStreak.icon,
                )
                StatisticsItem(
                    statisticTitle = statistics.averageTasks.title,
                    statisticsComment = statistics.averageTasks.hint,
                    icon = statistics.averageTasks.icon,
                )
            }
        }
    }
}

@Composable
fun StatisticsItem(
    statisticTitle: String,
    statisticsComment: String,
    @DrawableRes icon: Int,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = statisticTitle, style = MaterialTheme.typography.titleLarge)
            Text(
                text = statisticsComment,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
        )
    }
}

@Composable
private fun ScreenContent(
    habits: List<HabitUi>,
    statistics: StatisticsDataUi,
    calendarDataUi: CalendarDataUi,
    onNextMonthClicked: () -> Unit,
    onPreviousMonthClicked: () -> Unit,
    onCurrentDateClicked: () -> Unit,
    daySelected: (Int) -> Unit,
    addHabit: () -> Unit,
    deleteHabits: () -> Unit,
    deleteHabit: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    Surface(
        modifier = modifier,
        color = colorResource(id = R.color.background),
    ) {
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
) {
    LazyColumn(state = listState) {
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

@Composable
fun HabitItem(
    habit: HabitUi,
    onHabitClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = Color.White,
        modifier =
            modifier
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .fillMaxWidth()
                .height(90.dp)
                .clickable { onHabitClicked(habit.id) },
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            PriorityIndication(habit)
            HabitMainInfo(
                habit.name,
                habit.timeToDoIndication,
                habit.repetitionIndication,
                Modifier.weight(1f),
            )
            ProgressIndicator(habit.progress, Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
private fun PriorityIndication(habit: HabitUi) {
    Surface(
        modifier =
            Modifier
                .padding(end = 12.dp)
                .width(16.dp)
                .fillMaxHeight(1f),
        color = colorResource(habit.priorityIndicationColor),
    ) {}
}

@Composable
private fun HabitMainInfo(
    habitName: String,
    timeToDoIndication: String,
    repetitionIndication: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = timeToDoIndication,
                style = MaterialTheme.typography.bodySmall,
            )
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Habit repetition icon",
                tint = colorResource(R.color.orange),
                modifier =
                    Modifier
                        .padding(start = 8.dp, end = 2.dp)
                        .size(10.dp),
            )
            Text(
                text = "Sun, Tue, Thu",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.orange),
            )
        }
        Text(
            text = habitName,
            style = MaterialTheme.typography.titleLarge,
        )
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier =
                Modifier
                    .padding(top = 4.dp, bottom = 12.dp),
        ) {
            Text(
                text = repetitionIndication,
                style = MaterialTheme.typography.labelSmall,
                modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
                        .paddingFromBaseline(top = 8.dp, bottom = 4.dp),
            )
        }
    }
}

@Composable
private fun ProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .padding(end = 24.dp)
                .size(40.dp),
    ) {
        CircularProgressIndicator(
            progress = 1f,
            color = Color(0xFFD5D8DC),
        )
        CircularProgressIndicator(
            progress = progress,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}

@Preview
@Composable
fun StatisticsContentPreview() {
    StatisticsContent(getMockStatisticsDate())
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
fun HabitItemPreview() {
    val mockHabit =
        HabitUi(
            id = 0,
            name = "Go to the gym",
            timeToDoIndication = "10:00 AM",
            repetitionIndication = "10 times per day",
            0.3f,
            R.color.purple_200,
        )
    HabitsTheme {
        HabitItem(mockHabit, {})
    }
}

@Preview
@Composable
fun ScreenPreview() {
    val mockHabit =
        HabitUi(
            id = 0,
            name = "Go to the gym",
            timeToDoIndication = "10:00 AM",
            repetitionIndication = "10 times per day",
            0.3f,
            R.color.purple_200,
        )
    HabitsScreen(
        listOf(mockHabit),
        getMockStatisticsDate(),
        CalendarDataUi(),
        {},
        {},
        {},
        {},
        {},
        {},
        {},
    )
}

// TODO move this
private fun getMockStatisticsDate(): StatisticsDataUi {
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
