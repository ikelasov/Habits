package com.example.habits.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habits.R
import com.example.habits.ui.theme.HabitsTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("LongMethod")
@Composable
fun App() {
    HabitsTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
            ) {
                val datePickerState =
                    rememberDatePickerState(initialDate = LocalDate.of(2021, 5, 12))

                var mainBackgroundColor by remember { mutableStateOf(R.color.purple_500) }
                var selectedDateBackgroundColor by remember {
                    mutableStateOf(Color.Black.copy(alpha = 0.35f))
                }
                var eventIndicatorColor by remember {
                    mutableStateOf(Color.Black.copy(alpha = 0.35f))
                }
                var dateTextColor by remember { mutableStateOf(Color.White) }
                val today = LocalDate.now()

                DatePickerTimeline(
                    modifier = Modifier.wrapContentSize(),
                    onDateSelected = {},
                    backgroundColor = colorResource(mainBackgroundColor),
                    state = datePickerState,
                    orientation = com.example.habits.view.Orientation.Horizontal,
                    selectedBackgroundColor = selectedDateBackgroundColor,
                    selectedTextColor = Color.White,
                    dateTextColor = dateTextColor,
                    eventDates = listOf(
                        today.plusDays(1),
                        today.plusDays(3),
                        today.plusDays(5),
                        today.plusDays(8),
                    ),
                    todayLabel = {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "Today",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    },
                    pastDaysCount = 1,
                    eventIndicatorColor = eventIndicatorColor,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    App()
}