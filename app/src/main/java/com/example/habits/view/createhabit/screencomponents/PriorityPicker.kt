package com.example.habits.view.createhabit.screencomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.R
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun PriorityPicker(
    priorityLevel: HabitPriorityLevel,
    onPriorityLevelChanged: (HabitPriorityLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.clip(RoundedCornerShape(8.dp)).padding(8.dp),
    ) {
        Column {
            PriorityPickerItem(
                text = HabitPriorityLevel.TOP_PRIORITY.value,
                boxColor = colorResource(R.color.top_priority),
                isChecked = priorityLevel == HabitPriorityLevel.TOP_PRIORITY,
                onCheckedChanged = {
                    onPriorityLevelChanged(HabitPriorityLevel.TOP_PRIORITY)
                },
            )
            PriorityPickerItem(
                text = HabitPriorityLevel.HIGH_PRIORITY.value,
                boxColor = colorResource(R.color.high_priority),
                isChecked = priorityLevel == HabitPriorityLevel.HIGH_PRIORITY,
                onCheckedChanged = {
                    onPriorityLevelChanged(HabitPriorityLevel.HIGH_PRIORITY)
                },
            )
        }
        Column {
            PriorityPickerItem(
                text = HabitPriorityLevel.MEDIUM_PRIORITY.value,
                boxColor = colorResource(R.color.medium_priority),
                isChecked = priorityLevel == HabitPriorityLevel.MEDIUM_PRIORITY,
                onCheckedChanged = {
                    onPriorityLevelChanged(HabitPriorityLevel.MEDIUM_PRIORITY)
                },
            )
            PriorityPickerItem(
                text = HabitPriorityLevel.LOW_PRIORITY.value,
                boxColor = colorResource(R.color.low_priority),
                isChecked = priorityLevel == HabitPriorityLevel.LOW_PRIORITY,
                onCheckedChanged = {
                    onPriorityLevelChanged(HabitPriorityLevel.LOW_PRIORITY)
                },
            )
        }
    }
}

@Composable
fun PriorityPickerItem(
    text: String,
    boxColor: Color,
    isChecked: Boolean,
    onCheckedChanged: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onCheckedChanged()
            },
            colors =
                CheckboxDefaults.colors(
                    checkedColor = boxColor,
                    uncheckedColor = boxColor,
                ),
        )
        Text(text = text, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true)
@Composable
fun PriorityPickerPreview() {
    HabitsTheme {
        PriorityPicker(HabitPriorityLevel.MEDIUM_PRIORITY, {})
    }
}
