package com.example.habits

sealed class HabitsDestinations(val route: String) {
    data object HabitsScreen : HabitsDestinations(route = "habits")
    data object AddHabitScreen : HabitsDestinations(route = "add_habit")
}