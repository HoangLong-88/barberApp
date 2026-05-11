package com.example.barberapp.View.screenUI.customer.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberapp.View.component.MenuItemRow
import com.example.barberapp.View.component.SharedBottomNavBar
import com.example.barberapp.View.layout.StatsRow
import com.example.barberapp.View.layout.UserInfoRow
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.ViewModel.auth.AuthVM
import com.example.barberapp.ViewModel.customer.UserVM

// ── Data models ─────────────────────────────────────────────────────────────
data class StatItem(val value: String, val label: String)

data class MenuItem(
    val icon: ImageVector,
    val label: String,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit = {}
)

// ── Screen ───────────────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authVM: AuthVM,
    userVM: UserVM
) {
    val userInfo = userVM.userData
    if (userInfo == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        LaunchedEffect(Unit) { userVM.fetchUserProfile() }
        return
    }
    val stats = listOf(
        StatItem("12", "Bookings"),
        StatItem("5", "Reviews"),
        StatItem("320", "Points"),
    )
    val menuItems = listOf(
        MenuItem(
            Icons.Outlined.Edit, "Edit Profile",
            onClick = {
                navController.navigate("edit_profile") {
                }
            }),
        MenuItem(
            Icons.Outlined.DateRange, "My Bookings",
            onClick = {
                navController.navigate("booking") {
                    popUpTo("profile") {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }),
        MenuItem(
            Icons.Outlined.FavoriteBorder, "Favorites",
            onClick = {
                navController.navigate("favorite") {
                }
            }),
        MenuItem(
            Icons.Outlined.Notifications, "Notifications",
            onClick = {
                navController.navigate("notification") {
                    popUpTo("profile") {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }),
        MenuItem(
            Icons.AutoMirrored.Outlined.Logout, "Logout", isDestructive = true,
            onClick = { authVM.logOut(navController, userVM) }
        ),
    )

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = { SharedBottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Title
            Text(
                text = "Profile",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // ── User info row ──────────────────────────────────────────────
            UserInfoRow(
                name = userInfo.name ?: "Loading...",
                email = userInfo.email ?: "Loading...",
                phone = userInfo.phone ?: "No phone linked!"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Stats row ─────────────────────────────────────────────────
            StatsRow(stats = stats)

            Spacer(modifier = Modifier.height(20.dp))

            // ── Menu items ────────────────────────────────────────────────
            menuItems.forEachIndexed { index, item ->
                MenuItemRow(item = item)
                if (index < menuItems.lastIndex) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
//@Preview(showBackground = true, backgroundColor = 0xFF1C1C1C)
//@Composable
//fun ProfileScreenPreview() {
//    MaterialTheme {
//        ProfileScreen(navController = rememberNavController())
//    }
//}
