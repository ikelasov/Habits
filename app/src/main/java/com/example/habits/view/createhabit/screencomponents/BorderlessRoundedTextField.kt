package com.example.habits.view.createhabit.screencomponents

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.habits.R

@ExperimentalMaterial3Api
@Composable
fun BorderlessRoundedTextField(
    habitName: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = habitName,
        onValueChange = {
            onValueChanged(it)
        },
        placeholder = { Text(text = hint) },
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedPlaceholderColor = colorResource(R.color.slate_blue_gray),
                unfocusedPlaceholderColor = colorResource(R.color.slate_blue_gray),
            ),
        modifier =
            modifier
                .background(
                    color = Color.White,
                    shape = MaterialTheme.shapes.large,
                ),
    )
}

@Preview
@Composable
fun BorderlessRoundedTextFieldPreview() {
    BorderlessRoundedTextFieldPreview()
}
