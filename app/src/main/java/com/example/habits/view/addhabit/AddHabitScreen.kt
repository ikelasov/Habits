package com.example.habits.view.addhabit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habits.R
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.ui.theme.HabitsTheme
import com.example.habits.view.addhabit.screencomponents.AddHabitTopBar
import com.example.habits.view.addhabit.screencomponents.CreateHabitButton
import com.example.habits.view.addhabit.screencomponents.DayPicker
import com.example.habits.view.addhabit.screencomponents.HabitNameInput
import com.example.habits.view.addhabit.screencomponents.PriorityPicker
import com.example.habits.view.addhabit.screencomponents.RepetitionsPerDayComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    onBackArrowClicked: () -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            AddHabitTopBar(
                onBackArrowClicked = { onBackArrowClicked() },
                modifier = Modifier.padding(top = 8.dp),
            )
        },
        bottomBar = {
            CreateHabitButton(
                onAddHabitClicked = { viewModel.attemptCreateHabit() },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
            )
        },
    ) { paddingValues ->
        Content(
            viewState.habitName,
            viewState.daysToRepeat,
            viewState.repetitionsPerDay,
            viewState.priorityLevel,
            viewModel::onHabitNameChanged,
            viewModel::onDaysToRepeatChanged,
            viewModel::onRepetitionsNumberPerDayChanged,
            viewModel::onPriorityLevelChanged,
            Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun Content(
    habitName: String,
    daysToRepeat: List<DaysOfWeek>,
    repetitionsPerDay: Int,
    priorityLevel: HabitPriorityLevel,
    onHabitNameChanged: (String) -> Unit,
    onDaysToRepeatChanged: (DaysOfWeek, Boolean) -> Unit,
    onRepetitionsNumberPerDayChanged: (Int) -> Unit,
    onPriorityLevelChanged: (HabitPriorityLevel) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier =
            modifier
                .verticalScroll(
                    rememberScrollState(),
                )
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        HabitNameInput(
            habitName,
            onHabitNameValueChanged = onHabitNameChanged,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayPicker(
            daysToRepeat,
            onDayChecked = onDaysToRepeatChanged,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        RepetitionsPerDayComponent(
            repetitionsPerDay,
            onRepetitionsNumberPerDayChanged,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .heightIn(60.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        PriorityPicker(
            priorityLevel,
            onPriorityLevelChanged,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddHabitScreenPreview() {
    HabitsTheme {
        Surface(color = colorResource(R.color.background), modifier = Modifier.fillMaxSize()) {
            AddHabitScreen({})
        }
    }
}
