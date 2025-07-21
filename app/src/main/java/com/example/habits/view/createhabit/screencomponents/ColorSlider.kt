package com.example.habits.view.createhabit.screencomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.ui.theme.HabitsTheme

@Composable
fun ColorPickerSlider(
    modifier: Modifier = Modifier,
    initialValue: Float = 0f,
    onColorSelected: (Color) -> Unit
) {
    var sliderPosition by remember { mutableStateOf(initialValue) }

    val selectedColor = Color.hsv(sliderPosition * 360f, 1f, 1f)

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Color preview box
        Box(
            modifier = Modifier
                .size(20.dp)
                .padding(bottom = 8.dp)
                .background(selectedColor, shape = MaterialTheme.shapes.small)
        )

        // Gradient bar with the slider on top
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = List(13) { i -> Color.hsv(i * 30f, 1f, 1f) }
                    ),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Slider(
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    onColorSelected(Color.hsv(it * 360f, 1f, 1f))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = selectedColor,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Preview
@Composable
private fun ColorPickerSliderPreview() {
    HabitsTheme {
        ColorPickerSlider() {}
    }
}
