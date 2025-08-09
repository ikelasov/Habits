package com.example.habits

sealed class HabitsDestinations(val route: String) {
    data object HabitsScreen : HabitsDestinations(route = "habits")
    data object CreateHabitScreen : HabitsDestinations(route = "add_habit")
    data object LoginScreen : HabitsDestinations(route = "login")
    data object SignUpScreen : HabitsDestinations(route = "signup")
}