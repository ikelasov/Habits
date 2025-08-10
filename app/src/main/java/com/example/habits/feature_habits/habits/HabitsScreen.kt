@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.habits.feature_habits.habits

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habits.R
import com.example.habits.core.model.quotes.QuoteEntity
import com.example.habits.feature_habits.common.model.HabitUi
import com.example.habits.feature_habits.habits.screencomponents.HabitItem
import com.example.habits.feature_habits.habits.screencomponents.HorizontalCalendar
import com.example.habits.feature_habits.habits.screencomponents.LoadingScreen
import com.example.habits.feature_habits.habits.screencomponents.MotivationalQuoteComponent
import com.example.habits.feature_habits.habits.screencomponents.TopBar
import com.example.habits.feature_habits.habits.utils.getDaysOfMonth
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun HabitsScreen(
    onCreateHabitClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    habitsViewModel: HabitsViewModel = hiltViewModel(),
) {
    val viewState by habitsViewModel.viewState
        .collectAsStateWithLifecycle()

    if (viewState.loading) {
        LoadingScreen()
    } else {
        ScreenContent(
            habits = viewState.habits,
            quote = viewState.quote,
            calendarDataUi = viewState.calendarDataUi,
            onCreateHabitClicked = onCreateHabitClicked,
            onNextMonthClicked = habitsViewModel::onNextMonthClicked,
            onPreviousMonthClicked = habitsViewModel::onPreviousMonthClicked,
            onCurrentDateClicked = habitsViewModel::onCurrentDateClicked,
            onDayClicked = habitsViewModel::onDayClicked,
            onHabitItemDragged = habitsViewModel::onHabitItemDragged,
            onMenuClicked = onMenuClicked,
        )
    }
}

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
private fun ScreenContent(
    habits: List<HabitUi>,
    quote: QuoteEntity?,
    calendarDataUi: CalendarDataUi,
    onCreateHabitClicked: () -> Unit,
    onNextMonthClicked: () -> Unit,
    onPreviousMonthClicked: () -> Unit,
    onCurrentDateClicked: () -> Unit,
    onDayClicked: (Int) -> Unit,
    onHabitItemDragged: (String, DraggedDirection) -> Unit,
    onMenuClicked: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateHabitClicked() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            TopBar(onMenuClicked = onMenuClicked)
        },
    ) { contentPadding ->
        val listState = rememberLazyListState()
        Content(
            habits = habits,
            quote = quote,
            calendarDataUi = calendarDataUi,
            onNextMonthClicked = onNextMonthClicked,
            onPreviousMonthClicked = onPreviousMonthClicked,
            onCurrentDateClicked = onCurrentDateClicked,
            onDayClicked = onDayClicked,
            onHabitItemDragged = onHabitItemDragged,
            listState = listState,
            modifier = Modifier.padding(contentPadding),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    habits: List<HabitUi>,
    quote: QuoteEntity?,
    calendarDataUi: CalendarDataUi,
    onNextMonthClicked: () -> Unit,
    onPreviousMonthClicked: () -> Unit,
    onCurrentDateClicked: () -> Unit,
    onDayClicked: (Int) -> Unit,
    onHabitItemDragged: (String, DraggedDirection) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    if (isLandscape()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                HorizontalCalendar(
                    selectedMonth = calendarDataUi.selectedMonth,
                    onNextMonthClicked = onNextMonthClicked,
                    onPreviousMonthClicked = onPreviousMonthClicked,
                    onCurrentDateClicked = onCurrentDateClicked,
                    daysOfMonth = calendarDataUi.daysOfMonth,
                    onDayClicked = onDayClicked,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            items(habits) { habit ->
                HabitItem(
                    habit = habit,
                    onHabitItemDragged = onHabitItemDragged,
                )
            }
        }
    } else {
        LazyColumn(
            state = listState,
            modifier = modifier.background(MaterialTheme.colorScheme.background),
        ) {
            item { Spacer(modifier = Modifier.padding(vertical = 4.dp)) }
            item { MotivationalQuoteComponent(quote) }
            item { Spacer(modifier = Modifier.padding(vertical = 4.dp)) }
            stickyHeader {
                HorizontalCalendar(
                    selectedMonth = calendarDataUi.selectedMonth,
                    onNextMonthClicked = onNextMonthClicked,
                    onPreviousMonthClicked = onPreviousMonthClicked,
                    onCurrentDateClicked = onCurrentDateClicked,
                    daysOfMonth = calendarDataUi.daysOfMonth,
                    onDayClicked = onDayClicked,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item { Spacer(modifier = Modifier.padding(vertical = 8.dp)) }
            items(
                items = habits,
                key = { it.id },
            ) {
                HabitItem(
                    habit = it,
                    onHabitItemDragged = onHabitItemDragged,
                )
            }
        }
    }
}

@Composable
private fun CreateHabitButton(
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
    TopBar(onMenuClicked = {})
}

@Preview
@Composable
fun HabitItemPreview() {
    val mockHabit =
        HabitUi(
            id = "1",
            name = "Go to the gym",
            category = "Some",
            categoryColor = Color.Yellow,
            timeToDoIndication = "10:00 AM",
            daysToRepeat = "Mon,Sun",
            repetitionIndication = "10 times per day",
            0.3f,
            R.color.purple_200,
        )
    HabitsTheme {
        HabitItem(mockHabit, { _, _ -> })
    }
}

@PreviewLightDark
@Composable
fun ScreenPreview() {
    val mockHabit =
        HabitUi(
            id = "1",
            name = "Go to the gym",
            category = "Some other",
            categoryColor = Color.Cyan,
            timeToDoIndication = "10:00 AM",
            daysToRepeat = "Mon,Sun",
            repetitionIndication = "10 times per day",
            0.3f,
            R.color.purple_200,
        )
    HabitsTheme {
        ScreenContent(
            habits = listOf(element = mockHabit),
            quote = QuoteEntity(
                0,
                "We are what we repeatedly do. Excellence, then, is not an act, but a habit.",
                "Aristotle"
            ),
            calendarDataUi =
                CalendarDataUi(
                    selectedMonth = "February 2024",
                    daysOfMonth = getDaysOfMonth(2024, 2),
                ),
            onCreateHabitClicked = {},
            onNextMonthClicked = {},
            onPreviousMonthClicked = {},
            onCurrentDateClicked = {},
            onDayClicked = {},
            onHabitItemDragged = { _, _ -> },
            onMenuClicked = {}
        )
    }
}

// endregion
