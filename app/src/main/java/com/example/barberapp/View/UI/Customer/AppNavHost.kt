package com.example.barberapp.View.UI.Customer

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home")         { HomeScreen(navController = navController) }
        composable("booking")      { MyBookingsScreen(navController = navController) }
        composable("notification") { NotificationsScreen(navController = navController) }
        composable("profile")      { ProfileScreen(navController = navController) }
    }
}