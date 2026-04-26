package com.example.barberapp.View.UI.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.SurfaceColor
import com.example.barberapp.View.utils.TextSecondary

private data class NavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
private val navItems = listOf(
    NavItem("home",         "Home",          Icons.Outlined.Home),
    NavItem("booking",      "Bookings",      Icons.Outlined.CalendarMonth),
    NavItem("notification", "Notifications", Icons.Outlined.Notifications),
    NavItem("profile",      "Profile",       Icons.Outlined.Person),
)
@Composable
fun SharedBottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        containerColor = SurfaceColor,
        tonalElevation = 0.dp
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) GoldAccent else TextSecondary
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) GoldAccent else TextSecondary,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}