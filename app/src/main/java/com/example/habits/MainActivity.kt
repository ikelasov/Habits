@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.habits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.habits.view.HabitUi
import com.example.habits.view.HabitsViewModel
import com.example.habits.view.HabitsViewState
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
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }) { contentPadding ->
                        HabitsContent(
                            viewState,
                            habitsViewModel::addHabit,
                            habitsViewModel::deleteHabits,
                            habitsViewModel::deleteHabit,
                            Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp))
    }
}

@Composable
private fun HabitsContent(
    viewState: HabitsViewState,
    addHabit: () -> Unit,
    deleteHabits: () -> Unit,
    deleteHabit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    Surface(
        modifier = modifier,
        color = colorResource(id = R.color.background)
    ) {
        HabitsList(
            viewState.habits,
            onAddHabitClicked = { addHabit() },
            onDeleteClicked = { deleteHabits() },
            onHabitClicked = { deleteHabit(it) },
            listState
        )
    }
}

@Composable
private fun HabitsList(
    habits: List<HabitUi>,
    onAddHabitClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onHabitClicked: (Int) -> Unit,
    listState: LazyListState
) {
    LazyColumn(state = listState) {
        items(
            items = habits,
            key = { it.id },
        ) {
            HabitItem(
                habit = it,
                onHabitClicked = onHabitClicked
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
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onButtonClick() },
        modifier = modifier
    ) {
        Text(text = "Add")
    }
}

@Composable
private fun DeleteHabitsButton(
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = { onButtonClicked() }, modifier = modifier) {
        Text(text = "Delete")
    }
}

@Composable
fun HabitItem(
    habit: HabitUi,
    onHabitClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 24.dp)
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onHabitClicked(habit.id) }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .width(16.dp)
                    .fillMaxHeight(1f),
                color = colorResource(habit.priorityIndicationColor)
            ) {}
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = habit.timeToDoIndication,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Habit repetition icon",
                        modifier = Modifier
                            .padding(start = 8.dp, end = 2.dp)
                            .size(10.dp)
                    )
                    Text(
                        text = "Sun, Tue, Thu",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 12.dp),
                ) {
                    Text(
                        text = habit.repetitionIndication,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .paddingFromBaseline(top = 8.dp, bottom = 4.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(40.dp)
                    .align(Alignment.CenterVertically),
            ) {
                CircularProgressIndicator(
                    progress = 1f,
                    color = Color(0xFFD5D8DC)
                )
                CircularProgressIndicator(
                    progress = habit.progress
                )
            }
        }
    }
}

@Preview
@Composable
fun HabitItemPreview() {
    val mockHabit = HabitUi(
        id = 0,
        name = "Go to the gym",
        timeToDoIndication = "10:00 AM",
        repetitionIndication = "10 times per day",
        0.3f,
        R.color.purple_200
    )
    HabitsTheme {
        HabitItem(mockHabit, {})
    }
}