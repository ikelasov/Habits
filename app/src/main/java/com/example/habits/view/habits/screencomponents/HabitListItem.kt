package com.example.habits.view.habits.screencomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme
import com.example.habits.view.habits.HabitUi

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
            HabitPriorityIndication(habit)
            HabitMainInfo(
                habit.name,
                habit.timeToDoIndication,
                habit.daysToRepeat,
                habit.repetitionIndication,
                Modifier.weight(1f),
            )
            HabitProgressIndicator(habit.progress, Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
private fun HabitPriorityIndication(habit: HabitUi) {
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
    daysToRepeat: String,
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
                text = daysToRepeat,
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
private fun HabitProgressIndicator(
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
