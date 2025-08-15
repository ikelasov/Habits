package com.example.habits

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsApp(startDestination: String, onSignOut: () -> Unit) {
    HabitsTheme {
        val navController = rememberNavController()
        val navScrollState = rememberScrollState()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(navScrollState)
                ) {
                    Surface(
                        modifier = Modifier.height(200.dp),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Habits",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    HorizontalDivider()
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Home") },
                        selected = currentRoute == HabitsDestinations.HabitsScreen.route,
                        onClick = {
                            navController.navigate(HabitsDestinations.HabitsScreen.route)
                            scope.launch { drawerState.close() }
                        },
                        modifier = if (isLandscape()) Modifier.safeContentPadding() else Modifier
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.AutoMirrored.Default.List, contentDescription = null) },
                        label = { Text("Manage Habits") },
                        selected = currentRoute == HabitsDestinations.ManageHabitsScreen.route,
                        onClick = {
                            navController.navigate(HabitsDestinations.ManageHabitsScreen.route)
                            scope.launch { drawerState.close() }
                        },
                        modifier = if (isLandscape()) Modifier.safeContentPadding() else Modifier
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.AutoMirrored.Default.List, contentDescription = null) },
                        label = { Text("Manage Categories") },
                        selected = currentRoute == HabitsDestinations.ManageCategoriesScreen.route,
                        onClick = {
                            navController.navigate(HabitsDestinations.ManageCategoriesScreen.route)
                            scope.launch { drawerState.close() }
                        },
                        modifier = if (isLandscape()) Modifier.safeContentPadding() else Modifier
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
                        },
                        modifier = if (isLandscape()) Modifier.safeContentPadding() else Modifier
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
