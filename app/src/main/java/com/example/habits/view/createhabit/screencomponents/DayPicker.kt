package com.example.habits.view.createhabit.screencomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.unit.sp
import com.example.habits.R
import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun DayPicker(
    daysToRepeat: List<DaysOfWeek>,
    onDayChecked: (DaysOfWeek, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier,
        color = Color.White,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Task Repeat")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ) {
                DaysOfWeek.values().forEach { dayOfWeek ->
                    DayPickerItem(
                        day = dayOfWeek,
                        isDayChecked =
                            daysToRepeat.any {
                                it == dayOfWeek
                            },
                        onDayChecked = onDayChecked,
                    )
                }
            }
        }
    }
}

@Composable
fun DayPickerItem(
    day: DaysOfWeek,
    isDayChecked: Boolean,
    onDayChecked: (DaysOfWeek, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = day.value.first().toString(),
            fontSize = 14.sp,
            color = colorResource(R.color.slate_blue_gray),
            modifier =
                Modifier
                    .padding(4.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Checkbox(
            checked = isDayChecked,
            onCheckedChange = {
                onDayChecked(day, !isDayChecked)
            },
            colors =
                CheckboxDefaults.colors(
                    uncheckedColor = colorResource(R.color.light_gray),
                    checkedColor = colorResource(R.color.orange),
                    checkmarkColor = Color.Transparent,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DayPickerItemPreview() {
    HabitsTheme {
        DayPickerItem(DaysOfWeek.MONDAY, true, { _, _ -> })
    }
}

@Preview
@Composable
fun DayPickerPreview() {
    HabitsTheme {
        DayPicker(listOf(DaysOfWeek.FRIDAY), { _, _ -> })
    }
}
