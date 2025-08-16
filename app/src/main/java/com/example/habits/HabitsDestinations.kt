package com.example.habits

const val HABIT_ID = "habitId"

sealed class HabitsDestinations(val route: String) {
    data object HabitsScreen : HabitsDestinations(route = "habits")
    data object CreateHabitScreen : HabitsDestinations(route = "add_habit?habitId={$HABIT_ID}") {
        fun createRoute(habitId: String?) = "add_habit?habitId=$habitId"
    }

    data object LoginScreen : HabitsDestinations(route = "login")
    data object SignUpScreen : HabitsDestinations(route = "signup")
    data object ManageHabitsScreen : HabitsDestinations(route = "manage_habits")
    data object ManageCategoriesScreen : HabitsDestinations(route = "manage_categories")
}