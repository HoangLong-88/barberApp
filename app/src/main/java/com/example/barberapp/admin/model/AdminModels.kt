package com.example.barberapp.admin.model

import androidx.compose.ui.graphics.Color

data class UserItem(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "",
    val roleColorHex: String = "#2196F3"
) {
    val roleColor: Color get() = try { Color(android.graphics.Color.parseColor(roleColorHex)) } catch (e: Exception) { Color(0xFF2196F3) }
}

data class ServiceItem(
    val id: String = "",
    val name: String = "",
    val duration: String = "",
    val price: String = "",
    val shopId: String = "" // Thêm trường này để biết dịch vụ thuộc tiệm nào
)

data class BookingItem(
    val id: String = "",
    val customerName: String = "",
    val serviceName: String = "",
    val barberName: String = "",
    val dateTime: String = "",
    val status: String = "Pending"
)

data class ShopItem(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val priceRange: String = "",
    val rating: String = "0",
    val imageUrl: String = ""
)
