package com.example.habits.feature_habits.createhabit.ui.screencomponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertHabitTopBar(
    onBackArrowClicked: () -> Unit,
    isInEditMode: Boolean,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = if (isInEditMode) stringResource(R.string.update_habit) else stringResource(R.string.create_a_new_habit),
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackArrowClicked() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CreateHabitTopBarPreview() {
    HabitsTheme {
        UpsertHabitTopBar(onBackArrowClicked = { }, true)
    }
}
