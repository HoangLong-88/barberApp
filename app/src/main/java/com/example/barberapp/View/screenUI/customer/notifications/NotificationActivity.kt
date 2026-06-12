package com.example.barberapp.View.screenUI.customer.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.Model.entities.Notification
import com.example.barberapp.Model.types.NotifType
import com.example.barberapp.View.component.NotificationCard
import com.example.barberapp.View.component.SharedBottomNavBar
import com.example.barberapp.View.utils.BackgroundColor
import com.example.barberapp.View.utils.PrimaryYellow
import com.example.barberapp.View.utils.TextPrimary

private val sampleNotifications = listOf<Notification>(
    Notification(
        1, NotifType.Confirmed, "Booking Confirmed",
        "Your appointment on 20 May at 17:00 is confirmed", "2 min ago", true
    ),
    Notification(
        2, NotifType.Reminder, "Reminder",
        "Your haircut appointment is tomorrow at 10:00", "1 hour ago", true
    ),
    Notification(
        3, NotifType.Offer, "Special Offer",
        "Get 20% off your next visit at King Barber Shop", "3 hours ago", false
    ),
    Notification(
        4, NotifType.Cancelled, "Booking Cancelled",
        "Your appointment on 15 May has been cancelled", "1 day ago", false
    )
)

// ─── Screen ──────────────────────────────────────────────────────────────────

@Composable
fun NotificationsScreen(navController: NavController) {
    var notifications by remember { mutableStateOf(sampleNotifications) }

    val unreadCount = notifications.count { it.isUnread }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = { SharedBottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))

            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Notifications",
                        color = TextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (unreadCount > 0) {
                        Text(
                            text = "$unreadCount unread",
                            color = PrimaryYellow,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                if (unreadCount > 0) {
                    Text(
                        text = "Mark all read",
                        color = PrimaryYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .clickable() {
                                notifications = notifications.map { it.copy(isUnread = false) }
                            }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── List ──────────────────────────────────────────────────────────
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(notifications, key = { it.id }) { notif ->
                    NotificationCard(
                        notification = notif,
                        onDelete = {
                            notifications = notifications.filter { it.id != notif.id }
                        }
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF121212, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen(navController = rememberNavController())
}



