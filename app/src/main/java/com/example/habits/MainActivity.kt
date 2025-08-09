package com.example.habits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.habits.data.habitscategory.CategoriesSyncManager
import com.example.habits.data.quotes.QuoteSyncManager
import com.example.habits.view.auth.AuthViewModel
import com.example.habits.ui.theme.HabitsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var quoteSyncManager: QuoteSyncManager

    @Inject
    lateinit var categoriesSyncManager: CategoriesSyncManager

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        enableEdgeToEdge()

        setContent {
            val authUiState by authViewModel.uiState.collectAsState()
            val currentUser = authUiState.currentUser

            val startDestination = if (currentUser != null) {
                quoteSyncManager.syncQuotesIfWeeklyIntervalPassed()
                categoriesSyncManager.startListeningForCategoryChanges(currentUser.uid)
                HabitsDestinations.HabitsScreen.route
            } else {
                categoriesSyncManager.stopListeningForCategoryChanges()
                HabitsDestinations.LoginScreen.route
            }

            HabitsApp(startDestination = startDestination)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        categoriesSyncManager.stopListeningForCategoryChanges()
    }
}

@Composable
fun HabitsApp(startDestination: String) {
    HabitsTheme {
        val navController = rememberNavController()
        HabitsNavHost(navController = navController, startDestination = startDestination)
    }
}
