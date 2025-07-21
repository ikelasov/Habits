package com.example.habits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.habits.ui.theme.HabitsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            enableEdgeToEdge()
            HabitsApp()
        }
    }
}

@Composable
fun HabitsApp() {
    HabitsTheme {
        val navController = rememberNavController()
        HabitsNavHost(navController = navController)
    }
}
