package com.example.habits.feature_habits.manage_habits

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habits.R
import com.example.habits.feature_habits.common.model.HabitUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageHabitsScreen(
    onMenuClicked: () -> Unit,
    viewModel: ManageHabitsViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.message) {
        viewModel.message.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Habits") },
                navigationIcon = {
                    IconButton(onClick = onMenuClicked) {
                        Icon(Icons.Filled.Menu, contentDescription = "Open navigation drawer")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (viewState.habits.isEmpty()) {
                Text(
                    text = "No habits yet. Create some!",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 8.dp
                    )
                ) {
                    items(viewState.habits) { habit ->
                        HabitListItem(habit = habit, onDeleteClicked = {
                            viewModel.onDeleteHabitClicked(habit.id)
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitListItem(
    habit: HabitUi,
    onDeleteClicked: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HabitPriorityIndication(habit = habit)
            HabitMainInfoContent(
                habitName = habit.name,
                categoryName = habit.category,
                categoryColor = habit.categoryColor,
                timeToDoIndication = habit.timeToDoIndication,
                daysToRepeat = habit.daysToRepeat,
                repetitionIndication = habit.repetitionIndication,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onDeleteClicked,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete habit")
            }
        }
    }
}

@Composable
private fun HabitPriorityIndication(habit: HabitUi) {
    Surface(
        modifier = Modifier
            .padding(end = 12.dp)
            .width(16.dp)
            .fillMaxHeight(),
        color = colorResource(habit.priorityIndicationColor),
    ) {}
}

@Composable
private fun HabitMainInfoContent(
    habitName: String,
    categoryName: String,
    categoryColor: Color?,
    timeToDoIndication: String,
    daysToRepeat: String,
    repetitionIndication: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.padding(top = 4.dp),
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
                modifier = Modifier
                    .padding(start = 8.dp, end = 2.dp)
                    .size(10.dp),
            )
            Text(
                text = daysToRepeat,
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.orange),
            )
        }
        Text(
            text = habitName,
            style = MaterialTheme.typography.titleLarge,
        )
        if (categoryName.isNotBlank() && categoryColor != null) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .border(
                        width = 1.dp,
                        color = categoryColor,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
        ) {
            Text(
                text = repetitionIndication,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .paddingFromBaseline(top = 8.dp, bottom = 4.dp),
            )
        }
    }
}
