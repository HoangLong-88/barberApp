package com.example.barberapp.Model.entities

data class EmployeeInfo(
    val id: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val rating: Double = 0.0,
    val totalRatings: Int = 0,
    val appointmentsToday: Int = 0,
    val completedToday: Int = 0
)

data class EmployeeBookingItem(
    val id: String = "",
    val customerName: String = "",
    val serviceName: String = "",
    val price: String = "",
    val time: String = "", // e.g., "17:00"
    val duration: Int = 30, // Thời gian làm (phút)
    val date: String = "", // e.g., "2024-03-25"
    val status: String = "Pending", // Pending, Confirmed, Completed, Cancelled
    val phone: String = "",
    val note: String = "" // Ghi chú từ khách hàng
)

data class DateItem(
    val dayOfWeek: String, // T2, T3...
    val dayOfMonth: String, // 25, 26...
    val fullDate: String, // yyyy-MM-dd
    val isSelected: Boolean = false
)
