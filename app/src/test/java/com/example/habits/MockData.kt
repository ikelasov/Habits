package com.example.habits

import com.example.habits.data.localdatasource.habits.DaysOfWeek
import com.example.habits.data.localdatasource.habits.HabitEntity
import com.example.habits.data.localdatasource.habits.HabitPriorityLevel
import com.example.habits.data.localdatasource.habits.TimeOfTheDay

val mockHabitEntities =
    listOf(
        HabitEntity(
            1,
            "Habit 1",
            TimeOfTheDay.ALL_DAY,
            listOf(DaysOfWeek.SUNDAY),
            2,
            0,
            HabitPriorityLevel.MEDIUM_PRIORITY,
        ),
        HabitEntity(
            2,
            "Habit 2",
            TimeOfTheDay.ALL_DAY,
            listOf(DaysOfWeek.MONDAY),
            5,
            2,
            HabitPriorityLevel.MEDIUM_PRIORITY,
        ),
        HabitEntity(
            3,
            "Habit 3",
            TimeOfTheDay.MORNING,
            listOf(DaysOfWeek.WEDNESDAY),
            4,
            2,
            HabitPriorityLevel.HIGH_PRIORITY,
        ),
        HabitEntity(
            4,
            "Habit 4",
            TimeOfTheDay.EVENING,
            listOf(DaysOfWeek.TUESDAY),
            5,
            2,
            HabitPriorityLevel.MEDIUM_PRIORITY,
        ),
    )
