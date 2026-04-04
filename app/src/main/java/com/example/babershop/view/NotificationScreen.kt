package com.example.babershop.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
// IMPORT MỚI
import com.example.babershop.model.NotificationItem
import com.example.babershop.controller.NotificationController

@Composable
fun NotificationsScreen(
    navController: NavController,
    controller: NotificationController = remember { NotificationController() }
) {
    val notifications = controller.notifications

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Notifications", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Text("${notifications.size} unread", color = Color(0xFFFFC107))
            }
            TextButton(onClick = { controller.markAllAsRead() }) {
                Text("Mark all read", color = Color(0xFFFFC107))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(notifications) { item ->
                NotificationCard(item) {
                    controller.removeNotification(item)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(item: NotificationItem, onDelete: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(item.icon, null, tint = item.iconColor, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, color = Color.White)
                Text(item.message, color = Color.Gray)
                Text(item.time, color = Color.Gray)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.Gray)
            }
        }
    }
}