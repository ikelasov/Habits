package com.example.habits.view.createhabit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habits.R
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.ui.theme.HabitsTheme
import com.example.habits.view.createhabit.screencomponents.CreateHabitButton
import com.example.habits.view.createhabit.screencomponents.CreateHabitTopBar
import com.example.habits.view.createhabit.screencomponents.DayPicker
import com.example.habits.view.createhabit.screencomponents.HabitExecutionTime
import com.example.habits.view.createhabit.screencomponents.HabitNameInput
import com.example.habits.view.createhabit.screencomponents.PriorityPicker
import com.example.habits.view.createhabit.screencomponents.RepetitionsPerDayComponent
import com.example.habits.view.createhabit.screencomponents.TimePickerWithDialog

@Composable
fun CreateHabitScreen(
    onBackArrowClicked: () -> Unit,
    onHabitCreated: () -> Unit,
    viewModel: CreateHabitViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    if (viewState.habitCreated) {
        onHabitCreated()
    }

    val context = LocalContext.current
    LaunchedEffect(viewState.errorMessage) {
        with(viewState.errorMessage) {
            if (this == null) {
                return@LaunchedEffect
            }

            snackBarHostState.showSnackbar(message = context.getString(this))
            viewModel.onSnackbarDismissed()
        }
    }

    ScreenContent(
        habitName = viewState.habitName,
        daysToRepeat = viewState.daysToRepeat,
        repetitionsPerDay = viewState.repetitionsPerDay,
        priorityLevel = viewState.priorityLevel,
        habitExecutionTime = viewState.habitExecutionTime,
        shouldShowTimePicker = viewState.shouldShowTimePicker,
        onBackArrowClicked = onBackArrowClicked,
        attemptCreateHabit = viewModel::attemptCreateHabit,
        onHabitNameChanged = viewModel::onHabitNameChanged,
        onDaysToRepeatChanged = viewModel::onDaysToRepeatChanged,
        onRepetitionsNumberPerDayChanged = viewModel::onRepetitionsNumberPerDayChanged,
        onChooseTimeClicked = viewModel::onChooseTimeClicked,
        onTimeChosen = viewModel::onTimeChosen,
        onDialogDismissed = viewModel::onDialogDismissed,
        onPriorityLevelChanged = viewModel::onPriorityLevelChanged,
        snackBarHostState = snackBarHostState,
    )
}

@Composable
private fun ScreenContent(
    habitName: String,
    daysToRepeat: List<DaysOfWeek>,
    repetitionsPerDay: Int,
    priorityLevel: HabitPriorityLevel,
    habitExecutionTime: String,
    shouldShowTimePicker: Boolean,
    onBackArrowClicked: () -> Unit,
    attemptCreateHabit: () -> Unit,
    onHabitNameChanged: (String) -> Unit,
    onDaysToRepeatChanged: (DaysOfWeek, Boolean) -> Unit,
    onRepetitionsNumberPerDayChanged: (Int) -> Unit,
    onChooseTimeClicked: () -> Unit,
    onTimeChosen: (Int, Int) -> Unit,
    onDialogDismissed: () -> Unit,
    onPriorityLevelChanged: (HabitPriorityLevel) -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = {
            CreateHabitTopBar(
                onBackArrowClicked = { onBackArrowClicked() },
                modifier = Modifier.padding(top = 8.dp),
            )
        },
        bottomBar = {
            CreateHabitButton(
                onCreateHabitClicked = { attemptCreateHabit() },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
    ) { paddingValues ->
        Content(
            habitName = habitName,
            daysToRepeat = daysToRepeat,
            repetitionsPerDay = repetitionsPerDay,
            priorityLevel = priorityLevel,
            habitExecutionTime = habitExecutionTime,
            onHabitNameChanged = onHabitNameChanged,
            onDaysToRepeatChanged = onDaysToRepeatChanged,
            onChooseTimeClicked = onChooseTimeClicked,
            onTimeChosen = onTimeChosen,
            onDialogDismissed = onDialogDismissed,
            onRepetitionsNumberPerDayChanged = onRepetitionsNumberPerDayChanged,
            onPriorityLevelChanged = onPriorityLevelChanged,
            shouldShowTimePicker = shouldShowTimePicker,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun Content(
    habitName: String,
    daysToRepeat: List<DaysOfWeek>,
    repetitionsPerDay: Int,
    priorityLevel: HabitPriorityLevel,
    habitExecutionTime: String,
    onHabitNameChanged: (String) -> Unit,
    onDaysToRepeatChanged: (DaysOfWeek, Boolean) -> Unit,
    onChooseTimeClicked: () -> Unit,
    onTimeChosen: (Int, Int) -> Unit,
    onDialogDismissed: () -> Unit,
    onRepetitionsNumberPerDayChanged: (Int) -> Unit,
    onPriorityLevelChanged: (HabitPriorityLevel) -> Unit,
    shouldShowTimePicker: Boolean,
    modifier: Modifier,
) {
    Box {
        if (shouldShowTimePicker) {
            TimePickerWithDialog(
                onTimeChosen = onTimeChosen,
                onDialogDismissed = onDialogDismissed,
            )
        }

        Column(
            modifier =
                modifier
                    .verticalScroll(
                        rememberScrollState(),
                    )
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (shouldShowTimePicker) {
            }
            Spacer(modifier = Modifier.height(16.dp))
            HabitNameInput(
                habitName = habitName,
                onHabitNameValueChanged = onHabitNameChanged,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            DayPicker(
                daysToRepeat = daysToRepeat,
                onDayChecked = onDaysToRepeatChanged,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            RepetitionsPerDayComponent(
                repetitionsPerDay = repetitionsPerDay,
                onRepetitionsNumberPerDayChanged = onRepetitionsNumberPerDayChanged,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .heightIn(60.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitExecutionTime(
                habitExecutionTime = habitExecutionTime,
                onChooseTimeClicked = onChooseTimeClicked,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .heightIn(60.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            PriorityPicker(
                priorityLevel = priorityLevel,
                onPriorityLevelChanged = onPriorityLevelChanged,
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateHabitScreenPreview() {
    HabitsTheme {
        Surface(color = colorResource(R.color.background), modifier = Modifier.fillMaxSize()) {
            val snackBarHostState = remember { SnackbarHostState() }
            ScreenContent(
                onBackArrowClicked = {},
                attemptCreateHabit = {},
                onHabitNameChanged = { _ -> },
                onDaysToRepeatChanged = { _, _ -> },
                onRepetitionsNumberPerDayChanged = { _ -> },
                onChooseTimeClicked = { },
                onTimeChosen = { _, _ -> },
                onDialogDismissed = { },
                onPriorityLevelChanged = { _ -> },
                snackBarHostState = snackBarHostState,
                habitName = "",
                daysToRepeat = listOf(DaysOfWeek.MONDAY),
                repetitionsPerDay = 3, priorityLevel = HabitPriorityLevel.MEDIUM_PRIORITY,
                habitExecutionTime = "8:00",
                shouldShowTimePicker = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateHabitScreenWithTimePickerPreview() {
    HabitsTheme {
        Surface(color = colorResource(R.color.background), modifier = Modifier.fillMaxSize()) {
            val snackBarHostState = remember { SnackbarHostState() }
            ScreenContent(
                onBackArrowClicked = {},
                attemptCreateHabit = {},
                onHabitNameChanged = { _ -> },
                onDaysToRepeatChanged = { _, _ -> },
                onRepetitionsNumberPerDayChanged = { _ -> },
                onChooseTimeClicked = { },
                onTimeChosen = { _, _ -> },
                onDialogDismissed = { },
                onPriorityLevelChanged = { _ -> },
                snackBarHostState = snackBarHostState,
                habitName = "",
                daysToRepeat = listOf(DaysOfWeek.MONDAY),
                repetitionsPerDay = 3, priorityLevel = HabitPriorityLevel.MEDIUM_PRIORITY,
                habitExecutionTime = "8:00",
                shouldShowTimePicker = true,
            )
        }
    }
}
