package com.example.habits.view.habits.mapper

import com.example.habits.R
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.view.habits.HabitUi

fun List<HabitEntity>.mapHabitEntityListToHabitUIList(): List<HabitUi> {
    return this.map {
        it.mapHabitEntityToHabitUI()
    }
}

fun HabitEntity.mapHabitEntityToHabitUI(): HabitUi {
    val timeToDoIndication = this.timeOfTheDay.value

    val repetitionIndication =
        "${this.repetitionsPerDay} time" + if (repetitionsPerDay > 1) "s" else "" + " per day"

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
        timeToDoIndication = timeToDoIndication,
        repetitionIndication = repetitionIndication,
        progress = progress,
        priorityIndicationColor = priorityIndicationColor,
    )
}
