package com.example.habits

sealed class HabitsDestinations(val route: String) {
    data object HabitsScreen : HabitsDestinations(route = "habits")
    data object CreateHabitScreen : HabitsDestinations(route = "add_habit")
    data object LoginScreen : HabitsDestinations(route = "login")
    data object SignUpScreen : HabitsDestinations(route = "signup")
    data object ManageHabitsScreen : HabitsDestinations(route = "manage_habits")
    data object ManageCategoriesScreen : HabitsDestinations(route = "manage_categories")
}