package com.example.habits.view.createhabit.screencomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun HabitExecutionTime(
    habitExecutionTime: String,
    onChooseTimeClicked: () -> Unit,
    modifier: Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier =
            modifier.clickable {
                onChooseTimeClicked()
            },
        color = Color.White,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp),
        ) {
            Text(text = stringResource(R.string.time), Modifier.weight(1f))
            Text(text = habitExecutionTime)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun HabitExecutionTimePreview() {
    HabitsTheme {
        HabitExecutionTime("9:00", {}, Modifier.heightIn(60.dp))
    }
}
