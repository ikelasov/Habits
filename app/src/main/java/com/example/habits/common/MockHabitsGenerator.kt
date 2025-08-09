package com.example.habits.common

import com.example.habits.data.model.habits.DaysOfWeek
import com.example.habits.data.model.habits.HabitEntity
import com.example.habits.data.model.habits.HabitPriorityLevel
import com.example.habits.data.model.habits.TimeOfTheDay
import java.time.LocalTime
import kotlin.random.Random

fun generateMockHabit(): HabitEntity {
    val habitName = listOfNames.random()
    val timeOfTheDay = TimeOfTheDay.values().random()
    val repetitionsPerDay = Random.nextInt(from = 1, until = 10)
    val completedRepetitions = Random.nextInt(from = 0, until = repetitionsPerDay)
    val habitPriorityLevel = HabitPriorityLevel.values().random()
    val reminderTimes =
        listOf<LocalTime>(
            LocalTime.now().plusSeconds(5),
            LocalTime.now().plusSeconds(10),
        )

    return HabitEntity(
        id = "11",
        userId = "userId",
        createdAt = System.currentTimeMillis(),
        name = habitName,
        timeOfTheDay = timeOfTheDay,
        repetitionsPerDay = repetitionsPerDay,
        daysToRepeat =
            listOf(
                DaysOfWeek.WEDNESDAY,
                DaysOfWeek.FRIDAY,
                DaysOfWeek.MONDAY,
                DaysOfWeek.SATURDAY,
            ),
        completedRepetitions = completedRepetitions,
        priorityLevel = habitPriorityLevel,
        reminderTimes = reminderTimes,
        categoryId = "1"
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
