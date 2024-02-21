package com.example.habits.view.createhabit.screencomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.ui.theme.HabitsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerWithDialog(
    onTimeChosen: (Int, Int) -> Unit,
    onDialogDismissed: () -> Unit,
) {
    val timeState =
        rememberTimePickerState(
            initialHour = 9,
            initialMinute = 0,
            is24Hour = true,
        )

    BasicAlertDialog(
        onDismissRequest = { onDialogDismissed() },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier.padding(
                        top = 28.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 12.dp,
                    ),
            ) {
                TimePicker(state = timeState)
                Row(
                    modifier =
                        Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            onDialogDismissed()
                        },
                    ) {
                        Text(text = "Dismiss")
                    }
                    TextButton(
                        onClick = {
                            onTimeChosen(timeState.hour, timeState.minute)
                        },
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TimePickerDialogPreview() {
    HabitsTheme {
        TimePickerWithDialog({ _, _ -> }, {})
    }
}
