package com.glc.smartcar.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.glc.smartcar.ui.auth.AuthScreen
import com.glc.smartcar.ui.components.navigation.BottomNavBarComponent
import com.glc.smartcar.ui.history.HistoryScreen
import com.glc.smartcar.ui.newevaluation.NewEvaluationScreen
import com.glc.smartcar.ui.profile.ProfileScreen

import com.glc.smartcar.ui.evaluationdetails.EvaluationDetailsScreen

@Composable
fun SmartCarNavHost(startDestination: Any) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Mostrar BottomBar apenas em telas do MainGraph
    val showBottomBar = currentDestination?.hierarchy?.any {
        val route = it.route ?: ""
        route.contains("AppRoute.History") ||
        route.contains("AppRoute.NewEvaluation") ||
        route.contains("AppRoute.Profile")
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBarComponent(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth Graph
            navigation<AppRoute.AuthGraph>(
                startDestination = AppRoute.Login
            ) {
                composable<AppRoute.Login> {
                    AuthScreen(navController = navController)
                }
            }

            // Main Graph
            navigation<AppRoute.MainGraph>(
                startDestination = AppRoute.History
            ) {
                composable<AppRoute.History> {
                    HistoryScreen()
                }
                
                composable<AppRoute.NewEvaluation> {
                    NewEvaluationScreen(
                        onNavigateToDetails = {
                            navController.navigate(AppRoute.EvaluationDetails)
                        }
                    )
                }
                
                composable<AppRoute.Profile> {
                    ProfileScreen()
                }

                composable<AppRoute.EvaluationDetails> {
                    EvaluationDetailsScreen()
                }
            }
        }
    }
}
