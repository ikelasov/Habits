package com.example.habits.view.addhabit.screencomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun CreateHabitButton(
    onAddHabitClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { onAddHabitClicked() },
        modifier =
            modifier
                .height(60.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.medium_priority),
            ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
    ) {
        Text(text = "Create habit", fontSize = 18.sp)
    }
}

@Preview
@Composable
fun AddHabitButtonPreview() {
    HabitsTheme {
        CreateHabitButton(
            {},
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }
}
