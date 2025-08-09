package com.example.habits.feature_habits.habits.mapper

import androidx.compose.ui.graphics.Color
import com.example.habits.R
import com.example.habits.core.model.habitcategory.HabitCategoryEntity
import com.example.habits.core.model.habits.HabitEntity
import com.example.habits.core.model.habits.HabitPriorityLevel
import com.example.habits.feature_habits.habits.HabitUi

fun List<HabitEntity>.mapHabitEntityListToHabitUIList(categories: List<HabitCategoryEntity>): List<HabitUi> {
    return this.map {
        val category = categories.find { category -> category.id == it.categoryId }
        it.mapHabitEntityToHabitUI(category)
    }
}

fun HabitEntity.mapHabitEntityToHabitUI(category: HabitCategoryEntity?): HabitUi {
    val timeToDoIndication = this.timeOfTheDay.value

    val repetitionIndication =
        "${this.repetitionsPerDay} time" + if (repetitionsPerDay > 1) "s" else "" + " per day"

    val daysToRepeat =
        if (this.daysToRepeat.size == 7) {
            "Every day"
        } else {
            this.daysToRepeat.joinToString(", ") {
                it.value.take(3)
            }
        }

    val progress = this.completedRepetitions.toFloat() / this.repetitionsPerDay.toFloat()

    val priorityIndicationColor =
        when (this.priorityLevel) {
            HabitPriorityLevel.TOP_PRIORITY -> R.color.top_priority
            HabitPriorityLevel.HIGH_PRIORITY -> R.color.high_priority
            HabitPriorityLevel.MEDIUM_PRIORITY -> R.color.medium_priority
            HabitPriorityLevel.LOW_PRIORITY -> R.color.low_priority
        }

    return HabitUi(
        id = this.id,
        name = this.name,
        category = category?.name ?: "",
        categoryColor = if (category?.color != null) Color(category.color) else null,
        timeToDoIndication = timeToDoIndication,
        daysToRepeat = daysToRepeat,
        repetitionIndication = repetitionIndication,
        progress = progress,
        priorityIndicationColor = priorityIndicationColor,
    )
}
