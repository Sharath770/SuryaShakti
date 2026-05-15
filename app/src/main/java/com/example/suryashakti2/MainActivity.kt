package com.example.suryashakti2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import com.example.suryashakti2.data.EnergyRepository
import com.example.suryashakti2.data.SuryaShaktiDatabase
import com.example.suryashakti2.viewmodel.*
import com.example.suryashakti2.ui.*
import com.example.suryashakti2.ui.theme.SuryaShaktiTheme

class MainActivity : ComponentActivity() {

    private val energyViewModel: EnergyViewModel by viewModels {
        val db = SuryaShaktiDatabase.getDatabase(applicationContext)

        EnergyViewModelFactory(
            EnergyRepository(
                db.energyLogDao(),
                db.userProfileDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SuryaShaktiTheme {

                val navController = rememberNavController()

                val profile by energyViewModel.userProfile.collectAsStateWithLifecycle()

                val startDestination =
                    if (profile == null) "onboarding" else "dashboard"

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        if (currentRoute != "onboarding") {
                            NavigationBar {

                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                                    label = { Text("Dashboard") },
                                    selected = currentRoute == "dashboard",
                                    onClick = {
                                        navController.navigate("dashboard") {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                )

                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.List, contentDescription = "History") },
                                    label = { Text("History") },
                                    selected = currentRoute == "history",
                                    onClick = {
                                        navController.navigate("history") {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                )

                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                    label = { Text("Profile") },
                                    selected = currentRoute == "profile",
                                    onClick = {
                                        navController.navigate("profile")
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        // 🔹 ONBOARDING
                        composable("onboarding") {
                            OnboardingScreen(
                                onSave = { capacity, rate, tariff ->
                                    energyViewModel.saveUserProfile(capacity, rate, tariff)

                                    navController.navigate("dashboard") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 🔥 UPDATED DASHBOARD (IMPORTANT CHANGE)
                        composable("dashboard") {
                            val uiState by energyViewModel.uiState.collectAsStateWithLifecycle()

                            EnergyDashboardScreen(
                                uiState = uiState,
                                onSolarGenerationChanged = energyViewModel::onSolarGenerationChanged,
                                onStartReadingChanged = energyViewModel::onStartReadingChanged,
                                onEndReadingChanged = energyViewModel::onEndReadingChanged,
                                onRolloverToggle = energyViewModel::onRolloverToggle,
                                onWeatherSelected = energyViewModel::onWeatherSelected,
                                onSaveDailyLog = energyViewModel::saveLog
                            )
                        }

                        // 🔹 HISTORY
                        composable("history") {
                            HistoryScreen(viewModel = energyViewModel)
                        }

                        // 🔹 PROFILE
                        composable("profile") {
                            val profile by energyViewModel.userProfile.collectAsStateWithLifecycle()

                            ProfileScreen(
                                profile = profile,
                                onSave = { c, r, t ->
                                    energyViewModel.saveUserProfile(c, r, t)
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}