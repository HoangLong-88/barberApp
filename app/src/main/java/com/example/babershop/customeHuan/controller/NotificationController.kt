package com.example.babershop.customeHuan.controller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.example.babershop.customeHuan.model.NotificationItem

class NotificationController {
    // Sử dụng mutableStateListOf để Compose có thể tự cập nhật khi xóa phần tử
    val notifications = mutableStateListOf(
        _root_ide_package_.com.example.babershop.customeHuan.model.NotificationItem(
            "Booking Confirmed",
            "Your appointment on 20 May at 17:00 is confirmed",
            "2 min ago",
            Icons.Default.CheckCircle,
            Color.Green
        ),
        _root_ide_package_.com.example.babershop.customeHuan.model.NotificationItem(
            "Reminder",
            "Your haircut appointment is tomorrow at 10:00",
            "1 hour ago",
            Icons.Default.AccessTime,
            Color(0xFFFFC107)
        ),
        _root_ide_package_.com.example.babershop.customeHuan.model.NotificationItem(
            "Special Offer",
            "Get 20% off your next visit",
            "3 hours ago",
            Icons.Default.Notifications,
            Color(0xFFFFC107)
        ),
        _root_ide_package_.com.example.babershop.customeHuan.model.NotificationItem(
            "Booking Cancelled",
            "Your appointment on 15 May has been cancelled",
            "1 day ago",
            Icons.Default.Close,
            Color.Red
        )
    )

    fun removeNotification(item: com.example.babershop.customeHuan.model.NotificationItem) {
        notifications.remove(item)
    }

    fun markAllAsRead() {
        // Logic xử lý khi nhấn "Mark all read"
    }
}