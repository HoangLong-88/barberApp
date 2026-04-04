package com.example.babershop.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
// IMPORT CONTROLLER
import com.example.babershop.controller.ProfileController

@Composable
fun ProfileScreen(
    navController: NavController,
    controller: ProfileController = remember { ProfileController() } // Khởi tạo controller
) {
    val user = controller.user // Lấy dữ liệu user từ controller

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(20.dp)
    ) {
        // Truyền dữ liệu vào Header
        ProfileHeader(user.name, user.email, user.phone)

        Spacer(modifier = Modifier.height(25.dp))

        // Truyền dữ liệu vào Stats
        StatsRow(user.bookingsCount, user.reviewsCount, user.points)

        Spacer(modifier = Modifier.height(25.dp))

        MenuItem("Edit Profile", Icons.Default.Edit) {
            navController.navigate("edit_profile")
        }
        MenuItem("My Bookings", Icons.Default.DateRange)
        MenuItem("Favorites", Icons.Default.FavoriteBorder) {
            navController.navigate("favorites")
        }
        MenuItem("Notifications", Icons.Default.Notifications) {
            navController.navigate("notifications")
        }
        MenuItem("Logout", Icons.Default.ExitToApp, color = Color.Red)
    }
}

@Composable
fun ProfileHeader(name: String, email: String, phone: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(70.dp).background(Color(0xFFE6B800), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, tint = Color.Black)
        }
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(text = name, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Text(text = email, color = Color.Gray)
            Text(text = phone, color = Color.Gray)
        }
    }
}

@Composable
fun StatsRow(bookings: String, reviews: String, points: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        StatItem(bookings, "Bookings")
        StatItem(reviews, "Reviews")
        StatItem(points, "Points")
    }
}

// ... Giữ nguyên các hàm StatItem và MenuItem như cũ ...