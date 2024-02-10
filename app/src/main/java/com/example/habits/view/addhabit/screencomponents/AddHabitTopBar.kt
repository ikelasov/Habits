package com.example.habits.view.addhabit.screencomponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitTopBar(
    onBackArrowClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Create a new habit", fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = { onBackArrowClicked() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        modifier = modifier,
        colors =
            TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(R.color.background),
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun AddHabitTopBarPreview() {
    HabitsTheme {
        AddHabitTopBar(onBackArrowClicked = {  })
    }
}
