package com.example.barberapp.View.screenUI.navHostApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.barberapp.View.screenUI.auth.LoginScreen
import com.example.barberapp.View.screenUI.auth.RegisterScreen
import com.example.barberapp.View.screenUI.customer.bookings.CustomerBookingsScreen
import com.example.barberapp.View.screenUI.customer.home.ShopDetailScreen
import com.example.barberapp.View.screenUI.customer.home.HomeScreen
import com.example.barberapp.View.screenUI.customer.notifications.NotificationsScreen
import com.example.barberapp.View.screenUI.customer.profile.EditProfileScreen
import com.example.barberapp.View.screenUI.customer.profile.FavoritesScreen
import com.example.barberapp.View.screenUI.customer.profile.ProfileScreen
import com.example.barberapp.View.screenUI.customer.reviews.WriteReviewScreen
import com.example.barberapp.ViewModel.AuthVM
import com.example.barberapp.ViewModel.UserVM
import com.example.barberapp.View.screenUI.admin.AdminDashboardScreen
import com.example.barberapp.View.screenUI.customer.bookings.BookingCheckoutScreen
import com.example.barberapp.View.screenUI.customer.bookings.BookingSuccessScreen
import com.example.barberapp.View.screenUI.employee.EmployeeScreen
import com.example.barberapp.ViewModel.BookingVM
import com.example.barberapp.ViewModel.ShopVM
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val userVM: UserVM = viewModel()
    val authVM: AuthVM = viewModel()
    val shopVM: ShopVM = viewModel()
    val bookingVM: BookingVM = viewModel()

    val userAcc = userVM.userData

    // Chỉ theo dõi userAcc — khi thay đổi thì navigate
    LaunchedEffect(userAcc) {
        val uid = userAcc?.id ?: ""
        if (userAcc != null && uid.isNotBlank()) {
            // Kick off fetch shops (không đợi)
            shopVM.init(uid)
            // Navigate ngay theo role
            val target = when (userAcc.role) {
                "manager"  -> "admin_graph"
                "employee" -> "emp_graph"
                else       -> "main_graph"
            }
            navController.navigate(target) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        } else if (userAcc == null) {
            // Logout hoặc chưa đăng nhập
            val hasSession = FirebaseAuth.getInstance().currentUser != null
            if (!hasSession) {
                navController.navigate("auth_graph") {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            // Nếu hasSession = true → UserVM đang fetch → chờ userAcc update
        }
    }

    NavHost(
        navController = navController,
        startDestination = "auth_graph"   // luôn bắt đầu từ auth
    ) {
        navigation(startDestination = "login", route = "auth_graph") {
            composable("login") { LoginScreen(navController = navController, authVM = authVM, userVM = userVM) }
            composable("register") { RegisterScreen(navController = navController, authVM = authVM) }
        }
        navigation(startDestination = "home", route = "main_graph") {
            composable("home") { HomeScreen(navController = navController, shopVM = shopVM, userVM = userVM) }
            composable("booking") { CustomerBookingsScreen(navController = navController, userId = userAcc?.id?: "", bookingVM = bookingVM) }
            composable("notification") { NotificationsScreen(navController = navController) }
            composable("profile") {ProfileScreen(navController = navController, authVM = authVM, userVM = userVM, shopVM = shopVM) }
            composable("edit_profile") { EditProfileScreen(navController = navController, userVM = userVM) }
            composable("favorite") { FavoritesScreen(navController = navController, shopVM = shopVM) }
            composable("shop_details/{shopId}") { backStackEntry ->
                val shopId = backStackEntry.arguments?.getString("shopId") ?: ""
                ShopDetailScreen(navController = navController, shopId = shopId, shopVM = shopVM)
            }
            composable("reviews/{shopId}") { backStackEntry ->
                val shopId = backStackEntry.arguments?.getString("shopId") ?: ""
                val shopDetailEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("shop_details/$shopId")
                }
                val shopVMLocal: ShopVM = viewModel(shopDetailEntry)
                WriteReviewScreen(
                    shopId = shopId,
                    userId = userAcc?.id ?: "",
                    userName = userAcc?.name ?: "Anonymous",
                    onBack = { navController.popBackStack() },
                    onSuccess = { shopVMLocal.loadReviewOnly(shopId) }
                )
            }
            composable(
                route = "booking_checkout/{shopId}/{serviceIds}",
                arguments = listOf(
                    navArgument("shopId") { type = NavType.StringType },
                    navArgument("serviceIds") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val shopId = backStackEntry.arguments?.getString("shopId") ?: ""
                val serviceIds = backStackEntry.arguments?.getString("serviceIds")?.split(",") ?: emptyList()
                BookingCheckoutScreen(navController = navController, shopId = shopId, initialServiceIds = serviceIds, userVM = userVM)
            }
            composable("booking_success/{bookingId}") { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                BookingSuccessScreen(navController = navController, bookingId = bookingId)
            }
        }
        navigation(startDestination = "admin", route = "admin_graph") {
            composable("admin") {
                AdminDashboardScreen(navController = navController, authVM = authVM, userVM = userVM, shopVM = shopVM)
            }
        }
        navigation(startDestination = "employee", route = "emp_graph") {
            composable("employee") {
                EmployeeScreen(navController = navController, authVM = authVM, userVM = userVM, shopVM = shopVM)
            }
        }
    }
}
