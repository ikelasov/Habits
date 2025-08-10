package com.example.habits

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habits.feature_habits.createhabit.ui.CreateHabitScreen
import com.example.habits.feature_login.LoginScreen
import com.example.habits.feature_login.SignUpScreen
import com.example.habits.feature_habits.habits.HabitsScreen
import com.example.habits.feature_habits.manage_categories.ManageCategoriesScreen
import com.example.habits.feature_habits.manage_habits.ManageHabitsScreen

@Composable
fun HabitsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String,
    onMenuClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composableWithAnimation(HabitsDestinations.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(HabitsDestinations.HabitsScreen.route) {
                        popUpTo(HabitsDestinations.LoginScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(HabitsDestinations.SignUpScreen.route)
                }
            )
        }
        composableWithAnimation(HabitsDestinations.SignUpScreen.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(HabitsDestinations.HabitsScreen.route) {
                        popUpTo(HabitsDestinations.SignUpScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composableWithAnimation(HabitsDestinations.HabitsScreen.route) {
            HabitsScreen(
                onCreateHabitClicked = {
                    navController.navigate(HabitsDestinations.CreateHabitScreen.route)
                },
                onMenuClicked = onMenuClick
            )
        }
        composableWithAnimation(HabitsDestinations.CreateHabitScreen.route) {
            CreateHabitScreen(
                onBackArrowClicked = {
                    navController.popBackStack()
                },
                onHabitCreated = {
                    navController.popBackStack(
                        HabitsDestinations.HabitsScreen.route,
                        inclusive = false,
                    )
                },
            )
        }
        composableWithAnimation(HabitsDestinations.ManageHabitsScreen.route) {
            ManageHabitsScreen()
        }
        composableWithAnimation(HabitsDestinations.ManageCategoriesScreen.route) {
            ManageCategoriesScreen()
        }
    }
}

fun NavGraphBuilder.composableWithAnimation(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(500),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(500),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(500),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(500),
            )
        },
    ) {
        content.invoke(this, it)
    }
}
