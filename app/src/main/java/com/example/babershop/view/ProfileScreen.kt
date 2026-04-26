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
import com.example.babershop.controller.ProfileController

@Composable
fun ProfileScreen(
    navController: NavController,
    controller: ProfileController = remember { ProfileController() }
) {
    val user = controller.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(20.dp)
    ) {
        ProfileHeader(user.name, user.email, user.phone)

        Spacer(modifier = Modifier.height(25.dp))

        StatsRow(user.bookingsCount, user.reviewsCount, user.points)

        Spacer(modifier = Modifier.height(25.dp))

        MenuItem(
            title = "Edit Profile",
            icon = Icons.Default.Edit
        ) {
            navController.navigate("edit_profile")
        }

        MenuItem(
            title = "My Bookings",
            icon = Icons.Default.DateRange
        ) {
            navController.navigate("bookings")
        }

        MenuItem(
            title = "Favorites",
            icon = Icons.Default.FavoriteBorder
        ) {
            navController.navigate("favorites")
        }

        MenuItem(
            title = "Notifications",
            icon = Icons.Default.Notifications
        ) {
            navController.navigate("notifications")
        }

        MenuItem(
            title = "Logout",
            icon = Icons.Default.ExitToApp,
            color = Color.Red
        ) {
            // Xử lý logic Logout tại đây nếu cần
        }
    }
}

@Composable
fun ProfileHeader(name: String, email: String, phone: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color(0xFFE6B800), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Column {
            Text(
                text = name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = email,
                color = Color.Gray
            )

            Text(
                text = phone,
                color = Color.Gray
            )
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

@Composable
fun StatItem(number: String, label: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        modifier = Modifier.size(width = 100.dp, height = 80.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(number, color = Color(0xFFE6B800), style = MaterialTheme.typography.titleMedium)
            Text(label, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun MenuItem(
    title: String,
    icon: ImageVector,
    color: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (color == Color.Red) Color.Red else Color(0xFFE6B800)
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(text = title, color = color)

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}