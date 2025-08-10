package com.example.habits.feature_habits.common.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class HabitUi(
    val id: String,
    val name: String,
    val category: String,
    val categoryColor: Color?,
    val timeToDoIndication: String,
    val daysToRepeat: String,
    val repetitionIndication: String,
    val progress: Float,
    val priorityIndicationColor: Int,
)
