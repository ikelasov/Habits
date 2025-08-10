package com.example.habits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habits.core.data.sync.MasterSyncManager
import com.example.habits.feature_login.AuthViewModel
import com.example.habits.ui.theme.HabitsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

            HabitsApp(
                startDestination = startDestination,
                onSignOut = {
                    authViewModel.signOut()
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        masterSyncManager.stopSync()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsApp(startDestination: String, onSignOut: () -> Unit) {
    HabitsTheme {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Surface(modifier = Modifier.height(200.dp)) {
                        // Placeholder for future content
                    }
                    HorizontalDivider()
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Home") },
                        selected = currentRoute == HabitsDestinations.HabitsScreen.route,
                        onClick = {
                            navController.navigate(HabitsDestinations.HabitsScreen.route)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.AutoMirrored.Default.List, contentDescription = null) },
                        label = { Text("Manage Habits") },
                        selected = currentRoute == HabitsDestinations.ManageHabitsScreen.route,
                        onClick = {
                            navController.navigate(HabitsDestinations.ManageHabitsScreen.route)
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.AutoMirrored.Default.List, contentDescription = null) },
                        label = { Text("Manage Categories") },
                        selected = currentRoute == HabitsDestinations.ManageCategoriesScreen.route,
                        onClick = {
                            navController.navigate(HabitsDestinations.ManageCategoriesScreen.route)
                            scope.launch { drawerState.close() }
                        }
                    )
                    Spacer(Modifier.weight(1f))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Default.ExitToApp,
                                contentDescription = null
                            )
                        },
                        label = { Text("Sign Out") },
                        selected = false,
                        onClick = {
                            onSignOut()
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        ) {
            HabitsNavHost(
                navController = navController,
                startDestination = startDestination,
                onMenuClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                })
        }
    }
}
