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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glc.smartcar.ui.theme.SmartCarTheme

enum class BottomNavItem(val title: String) {
    HISTORY("Histórico"),
    NEW_EVALUATION("Nova Avaliação"),
    PROFILE("Perfil")
}

@Composable
fun BottomNavBarComponent(
    currentRoute: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == BottomNavItem.HISTORY,
            onClick = { onItemSelected(BottomNavItem.HISTORY) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = BottomNavItem.HISTORY.title
                )
            },
            label = {
                Text(
                    text = BottomNavItem.HISTORY.title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (currentRoute == BottomNavItem.HISTORY) FontWeight.Bold else FontWeight.Medium,
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
            selected = currentRoute == BottomNavItem.NEW_EVALUATION,
            onClick = { onItemSelected(BottomNavItem.NEW_EVALUATION) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = BottomNavItem.NEW_EVALUATION.title
                )
            },
            label = {
                Text(
                    text = BottomNavItem.NEW_EVALUATION.title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (currentRoute == BottomNavItem.NEW_EVALUATION) FontWeight.Bold else FontWeight.Medium,
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
            selected = currentRoute == BottomNavItem.PROFILE,
            onClick = { onItemSelected(BottomNavItem.PROFILE) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = BottomNavItem.PROFILE.title
                )
            },
            label = {
                Text(
                    text = BottomNavItem.PROFILE.title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (currentRoute == BottomNavItem.PROFILE) FontWeight.Bold else FontWeight.Medium,
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
                    currentRoute = BottomNavItem.HISTORY,
                    onItemSelected = {}
                )
            }
        }
    }
}
