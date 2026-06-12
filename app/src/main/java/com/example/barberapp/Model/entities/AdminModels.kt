package com.example.barberapp.Model.entities

import androidx.compose.ui.graphics.Color

data class UserItem(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val role: String = "",
    val roleColorHex: String = "#2196F3"
) {
    @Suppress("DEPRECATION")
    val roleColor: Color get() = try { Color(android.graphics.Color.parseColor(roleColorHex)) } catch (_: Exception) { Color(0xFF2196F3) }
}


data class ServiceItem(
    val id: String = "",
    val name: String = "",
    val duration: String = "",
    val price: String = "",
    val shopId: String = ""
)

data class BookingItem(
    val id: String = "",
    val customerName: String = "",
    val serviceName: String = "",
    val barberName: String = "",
    val dateTime: String = "",
    val status: String = "Pending"
)
