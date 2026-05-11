package com.example.barberapp.Model.entities

import com.example.barberapp.Model.types.NotifType

data class Notification(
    val id: Int,
    val type: NotifType,
    val title: String,
    val body: String,
    val timeAgo: String,
    val isUnread: Boolean
)