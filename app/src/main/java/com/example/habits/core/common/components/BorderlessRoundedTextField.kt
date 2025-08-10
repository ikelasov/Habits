package com.example.habits.core.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.VisualTransformation // Import VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorderlessRoundedTextField(
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None // Add visualTransformation
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        placeholder = { Text(text = hint) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedPlaceholderColor = colorResource(R.color.slate_blue_gray),
            unfocusedPlaceholderColor = colorResource(R.color.slate_blue_gray),
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large,
            ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation // Use the parameter
    )
}

// Preview remains the same
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BorderlessRoundedTextFieldPreview() {
    HabitsTheme {
        BorderlessRoundedTextField(
            value = "Sample Text",
            hint = "Enter text here",
            onValueChanged = {}
        )
    }
}