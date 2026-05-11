package com.example.barberapp.View.screenUI.customer

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.screenUI.auth.LoginScreen
import com.example.barberapp.View.screenUI.auth.RegisterScreen
import com.example.barberapp.View.screenUI.customer.bookings.MyBookingsScreen
import com.example.barberapp.View.screenUI.customer.home.HomeScreen
import com.example.barberapp.View.screenUI.customer.notifications.NotificationsScreen
import com.example.barberapp.View.screenUI.customer.profile.EditProfileScreen
import com.example.barberapp.View.screenUI.customer.profile.FavoritesScreen
import com.example.barberapp.View.screenUI.customer.profile.ProfileScreen
import com.example.barberapp.ViewModel.auth.AuthVM
import com.example.barberapp.ViewModel.customer.UserVM
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val userVM: UserVM = viewModel()
    val authVM: AuthVM = viewModel()

    // Kiểm tra xem đã có user chưa để quyết định điểm bắt đầu (Root)
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startRoot = if (currentUser != null) "main_graph" else "auth_graph"

    NavHost(
        navController = navController,
        startDestination = startRoot // Điều hướng thẳng vào cụm tương ứng
    ) {
        navigation(startDestination = "login", route = "auth_graph") {
            composable("login") {
                LoginScreen(
                    navController = navController,
                    authVM = authVM
                )
            }
            composable("register") {
                RegisterScreen(
                    navController = navController,
                    authVM = authVM
                )
            }
        }

        navigation(startDestination = "home", route = "main_graph") {
            composable("home") { HomeScreen(navController = navController) }
            composable("booking") { MyBookingsScreen(navController = navController) }
            composable("notification") { NotificationsScreen(navController = navController) }
            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    authVM = authVM, userVM = userVM
                )
            }
            composable("edit_profile") {
                EditProfileScreen(
                    navController = navController,
                    userVM = userVM
                )
            }
            composable( "favorite" ){ FavoritesScreen(navController = navController) }
        }
    }
}