package com.example.habits.view.addhabit.screencomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habits.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitNameInput(
    habitName: String,
    onHabitNameValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = "Habit name", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        BorderlessRoundedTextField(
            habitName,
            hint = "Take proteins",
            onValueChanged = onHabitNameValueChanged,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HabitNamePreview() {
    Surface(color = colorResource(id = R.color.background)) {
        HabitNameInput("Habit name", {}, Modifier.padding(horizontal = 24.dp))
    }
}
