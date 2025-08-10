package com.example.habits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.habits.core.data.sync.MasterSyncManager
import com.example.habits.ui.theme.HabitsTheme
import com.example.habits.feature_login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var masterSyncManager: MasterSyncManager

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        enableEdgeToEdge()

        setContent {
            val authUiState by authViewModel.uiState.collectAsState()
            val currentUser = authUiState.currentUser

            LaunchedEffect(currentUser) {
                if (currentUser != null) {
                    masterSyncManager.startSync(currentUser.uid)
                } else {
                    masterSyncManager.stopSync()
                }
            }

            val startDestination = if (currentUser != null) {
                HabitsDestinations.HabitsScreen.route
            } else {
                HabitsDestinations.LoginScreen.route
            }

            HabitsApp(startDestination = startDestination)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        masterSyncManager.stopSync()
    }
}

@Composable
fun HabitsApp(startDestination: String) {
    HabitsTheme {
        val navController = rememberNavController()
        HabitsNavHost(navController = navController, startDestination = startDestination)
    }
}
