package com.example.habits

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habits.view.addhabit.AddHabitScreen
import com.example.habits.view.habits.HabitsScreen

@Composable
fun HabitsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HabitsDestinations.HabitsScreen.route,
        modifier = modifier,
    ) {
        composable(route = HabitsDestinations.HabitsScreen.route) {
            HabitsScreen(
                onAddHabitClicked = {
                    navController.navigate(HabitsDestinations.AddHabitScreen.route)
                },
            )
        }
        composable(route = HabitsDestinations.AddHabitScreen.route) {
            AddHabitScreen(
                onBackArrowClicked = {
                    navController.popBackStack()
                },
                onHabitCreated = {
                    navController.popBackStack(HabitsDestinations.HabitsScreen.route, inclusive = false)
                },
            )
        }
    }
}
