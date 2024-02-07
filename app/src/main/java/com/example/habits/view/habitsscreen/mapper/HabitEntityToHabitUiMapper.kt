package com.example.habits.view.habitsscreen.mapper

import com.example.habits.R
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay
import com.example.habits.view.habitsscreen.HabitUi

fun HabitEntity.mapHabitEntityToHabitUI(): HabitUi {
    val timeToDoIndication = when (this.timeOfTheDay) {
        TimeOfTheDay.MORNING -> "9:00 AM"
        TimeOfTheDay.NOUN -> "12:00 PM"
        TimeOfTheDay.EVENING -> "9:00 PM"
        TimeOfTheDay.ALL_DAY -> "All day"
    }

    val repetitionIndication = "${this.repetitionsPerDay.toInt()} times per day"
    val progress = this.completedRepetitions / this.repetitionsPerDay
    val priorityIndicationColor = when (this.priorityLevel) {
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
        priorityIndicationColor = priorityIndicationColor
    )
}