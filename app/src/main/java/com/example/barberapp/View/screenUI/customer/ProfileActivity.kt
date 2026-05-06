package com.example.barberapp.View.screenUI.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.component.MenuItemRow
import com.example.barberapp.View.component.SharedBottomNavBar
import com.example.barberapp.View.layout.StatsRow
import com.example.barberapp.View.layout.UserInfoRow
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.TextPrimary
import com.google.firebase.auth.FirebaseAuth

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
    onLogOut: () -> Unit = {}
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val stats = listOf(
        StatItem("12",  "Bookings"),
        StatItem("5",   "Reviews"),
        StatItem("320", "Points"),
    )

    val menuItems = listOf(
        MenuItem(Icons.Outlined.Edit,"Edit Profile"),
        MenuItem(Icons.Outlined.DateRange,"My Bookings"),
        MenuItem(Icons.Outlined.FavoriteBorder,"Favorites"),
        MenuItem(Icons.Outlined.Notifications,"Notifications"),
        MenuItem(Icons.AutoMirrored.Outlined.Logout, "Logout", isDestructive = true, onClick = onLogOut),
    )

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar     = { SharedBottomNavBar(navController) }
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
                text       = "Profile",
                color      = TextPrimary,
                fontSize   = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier   = Modifier.padding(bottom = 24.dp)
            )

            // ── User info row ──────────────────────────────────────────────
            UserInfoRow(
                name  = currentUser?.displayName?:"User",
                email = currentUser?.email?:"No email",
                phone = "090xxxxxxx"
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
@Preview(showBackground = true, backgroundColor = 0xFF1C1C1C)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(navController = rememberNavController())
    }
}
