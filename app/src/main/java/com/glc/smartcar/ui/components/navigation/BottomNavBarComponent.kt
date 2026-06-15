package com.glc.smartcar.ui.components.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.glc.smartcar.ui.navigation.AppRoute
import com.glc.smartcar.ui.theme.SmartCarTheme

@Composable
fun BottomNavBarComponent(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isHistorySelected = currentDestination?.hierarchy?.any { it.route?.contains("AppRoute.History") == true } == true
    val isNewEvalSelected = currentDestination?.hierarchy?.any { it.route?.contains("AppRoute.NewEvaluation") == true } == true
    val isProfileSelected = currentDestination?.hierarchy?.any { it.route?.contains("AppRoute.Profile") == true } == true

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = isHistorySelected,
            onClick = {
                navController.navigate(AppRoute.History) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Histórico"
                )
            },
            label = {
                Text(
                    text = "Histórico",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isHistorySelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 10.sp
                    )
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.outline,
                unselectedTextColor = MaterialTheme.colorScheme.outline,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            )
        )

        NavigationBarItem(
            selected = isNewEvalSelected,
            onClick = {
                navController.navigate(AppRoute.NewEvaluation) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = "Nova Avaliação"
                )
            },
            label = {
                Text(
                    text = "Nova Avaliação",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isNewEvalSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 10.sp
                    )
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.outline,
                unselectedTextColor = MaterialTheme.colorScheme.outline,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            )
        )

        NavigationBarItem(
            selected = isProfileSelected,
            onClick = {
                navController.navigate(AppRoute.Profile) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Perfil"
                )
            },
            label = {
                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isProfileSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 10.sp
                    )
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.outline,
                unselectedTextColor = MaterialTheme.colorScheme.outline,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            )
        )
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
private fun BottomNavBarComponentPreview() {
    SmartCarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(16.dp)) {
                BottomNavBarComponent(
                    navController = rememberNavController()
                )
            }
        }
    }
}
