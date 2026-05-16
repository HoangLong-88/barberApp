package com.example.barberapp.View.screenUI.navHostApp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.screenUI.auth.LoginScreen
import com.example.barberapp.View.screenUI.auth.RegisterScreen
import com.example.barberapp.View.screenUI.customer.bookings.MyBookingsScreen
import com.example.barberapp.View.screenUI.customer.home.ShopDetailScreen
import com.example.barberapp.View.screenUI.customer.home.HomeScreen
import com.example.barberapp.View.screenUI.customer.notifications.NotificationsScreen
import com.example.barberapp.View.screenUI.customer.profile.EditProfileScreen
import com.example.barberapp.View.screenUI.customer.profile.FavoritesScreen
import com.example.barberapp.View.screenUI.customer.profile.ProfileScreen
import com.example.barberapp.View.screenUI.customer.reviews.WriteReviewScreen
import com.example.barberapp.ViewModel.auth.AuthVM
import com.example.barberapp.ViewModel.customer.UserVM
import com.example.barberapp.View.screenUI.admin.AdminDashboardScreen
import com.example.barberapp.View.screenUI.employee.EmployeeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val userVM: UserVM = viewModel()
    val authVM: AuthVM = viewModel()

    var isLoading by remember { mutableStateOf(true) }
    var startRoot by remember { mutableStateOf("auth_graph") }
    val userAcc = userVM.userData

    LaunchedEffect(Unit) {
        if (userAcc != null) {
            val role = userAcc.role
            startRoot =
                when (role) {
                    "manager" -> "admin_graph"
                    "employee" -> "emp_graph"
                    else -> "main_graph"
                }
            isLoading = false
        } else {
            startRoot = "auth_graph"
            isLoading = false
        }
    }
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
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
                composable("favorite") { FavoritesScreen(navController = navController) }
                composable("shop_details") { ShopDetailScreen(navController = navController) }
                composable("reviews") { WriteReviewScreen() }
            }
            navigation(startDestination = "admin", route = "admin_graph") {
                composable("admin") {
                    AdminDashboardScreen(
                        navController = navController,
                        authVM = authVM, userVM = userVM
                    )
                }
            }
            navigation(startDestination = "employee", route = "emp_graph"){
                composable("employee") {
                    EmployeeScreen(
                        navController = navController,
                        authVM = authVM, userVM = userVM
                    )
                }
            }
        }
    }
}