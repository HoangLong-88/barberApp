package com.example.babershop.customeHuan.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NotificationItem(
    val title: String,
    val message: String,
    val time: String,
    val icon: ImageVector,
    val iconColor: Color
)