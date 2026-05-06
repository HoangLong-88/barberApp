package com.example.barberapp.Model.data

import com.example.barberapp.Model.types.NotifType

data class AppNofitifcation(
    val id: Int,
    val type: NotifType,
    val title: String,
    val body: String,
    val timeAgo: String,
    val isUnread: Boolean
)