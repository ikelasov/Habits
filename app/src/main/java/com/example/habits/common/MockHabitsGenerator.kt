package com.example.habits.common

import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay
import kotlin.random.Random

fun generateMockHabit(): HabitEntity {
    val habitName = listOfNames.random()
    val timeOfTheDay = TimeOfTheDay.values().random()
    val repetitionsPerDay = Random.nextInt(from = 1, until = 10)
    val completedRepetitions = Random.nextInt(from = 0, until = repetitionsPerDay)
    val habitPriorityLevel = HabitPriorityLevel.values().random()

    return HabitEntity(
        name = habitName,
        timeOfTheDay = timeOfTheDay,
        repetitionsPerDay = repetitionsPerDay,
        daysToRepeat = listOf(DaysOfWeek.FRIDAY, DaysOfWeek.MONDAY),
        completedRepetitions = completedRepetitions,
        priorityLevel = habitPriorityLevel,
    )
}

val listOfNames =
    listOf(
        "Go to the gym",
        "Drink water",
        "Clean bedroom",
        "Meditate",
        "Eat a frog",
    )
