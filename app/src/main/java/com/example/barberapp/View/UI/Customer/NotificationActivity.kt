package com.example.barberapp.View.UI.Customer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.Model.data.AppNofitifcation
import com.example.barberapp.Model.types.NotifType
import com.example.barberapp.View.UI.component.SharedBottomNavBar
import com.example.barberapp.View.utils.BackgroundColor
import com.example.barberapp.View.utils.CancelledIconBg
import com.example.barberapp.View.utils.CancelledIconFg
import com.example.barberapp.View.utils.ConfirmedIconBg
import com.example.barberapp.View.utils.ConfirmedIconFg
import com.example.barberapp.View.utils.OfferIconBg
import com.example.barberapp.View.utils.OfferIconFg
import com.example.barberapp.View.utils.PrimaryYellow
import com.example.barberapp.View.utils.ReminderIconBg
import com.example.barberapp.View.utils.ReminderIconFg
import com.example.barberapp.View.utils.SurfaceColor
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary

private val sampleNotifications = listOf<AppNofitifcation>(
    AppNofitifcation(
        1, NotifType.Confirmed, "Booking Confirmed",
        "Your appointment on 20 May at 17:00 is confirmed", "2 min ago", true
    ),
    AppNofitifcation(
        2, NotifType.Reminder, "Reminder",
        "Your haircut appointment is tomorrow at 10:00", "1 hour ago", true
    ),
    AppNofitifcation(
        3, NotifType.Offer, "Special Offer",
        "Get 20% off your next visit at King Barber Shop", "3 hours ago", false
    ),
    AppNofitifcation(
        4, NotifType.Cancelled, "Booking Cancelled",
        "Your appointment on 15 May has been cancelled", "1 day ago", false
    )
)

// ─── Helpers ─────────────────────────────────────────────────────────────────

private fun notifColors(type: NotifType): Pair<Color, Color> = when (type) {
    NotifType.Confirmed -> ConfirmedIconBg to ConfirmedIconFg
    NotifType.Reminder  -> ReminderIconBg to ReminderIconFg
    NotifType.Offer     -> OfferIconBg to OfferIconFg
    NotifType.Cancelled -> CancelledIconBg to CancelledIconFg
}

private fun notifIcon(type: NotifType): ImageVector = when (type) {
    NotifType.Confirmed -> Icons.Outlined.CheckCircle
    NotifType.Reminder  -> Icons.Outlined.AccessTime
    NotifType.Offer     -> Icons.Outlined.LocalOffer
    NotifType.Cancelled -> Icons.Outlined.Cancel
}

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

// ─── Notification Card ────────────────────────────────────────────────────────

@Composable
fun NotificationCard(
    notification: AppNofitifcation,
    onDelete: () -> Unit
) {
    val (iconBg, iconFg) = notifColors(notification.type)
    val icon = notifIcon(notification.type)

    // Animate card background: slightly brighter when unread
    val cardBg by animateColorAsState(
        targetValue = if (notification.isUnread) Color(0xFF262626) else SurfaceColor,
        animationSpec = tween(300),
        label = "cardBg"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bubble
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = notification.type.name,
                    tint = iconFg,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = notification.body,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.timeAgo,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    if (notification.isUnread) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(PrimaryYellow)
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // Delete button
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete notification",
                tint = TextSecondary,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDelete() }
                    .align(Alignment.Top)
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF121212, showSystemUi = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen(navController = rememberNavController())
}



