package com.example.habits.feature_habits.createhabit.ui.screencomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun CreateCategoryDialog(
    onConfirm: (String, Color) -> Unit,
    onDismiss: () -> Unit
) {
    var categoryName by rememberSaveable { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color.Red) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "New Category") },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Pick a color", style = MaterialTheme.typography.labelMedium)

                Spacer(modifier = Modifier.height(8.dp))

                ColorPickerSlider(
                    onColorSelected = { selectedColor = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (categoryName.isNotBlank()) {
                        onConfirm(categoryName, selectedColor)
                    }
                },
                enabled = categoryName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun CreateCategoryDialogPreview() {
    HabitsTheme {
        CreateCategoryDialog({ _, _ -> }, {})
    }
}