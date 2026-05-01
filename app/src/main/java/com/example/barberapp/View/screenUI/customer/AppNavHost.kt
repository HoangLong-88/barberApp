package com.example.barberapp.View.screenUI.customer

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.screenUI.auth.LoginScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("login") { LoginScreen() }
        composable("home") { HomeScreen(navController = navController) }
        composable("booking") { MyBookingsScreen(navController = navController) }
        composable("notification") { NotificationsScreen(navController = navController) }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                onLogOut = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login"){
                        popUpTo(0){inclusive = true}
                    }
                }
            )
        }
    }
}