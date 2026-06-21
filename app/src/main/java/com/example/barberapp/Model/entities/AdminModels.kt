package com.example.barberapp.Model.entities

data class BookingItem(
    val id: String = "",
    val customerName: String = "",
    val serviceName: String = "",
    val barberName: String = "",
    val dateTime: String = "",
    val status: String = "Pending"
)

data class AuditLog(
    val id: String = "",
    val adminId: String = "",
    val adminName: String = "",
    val action: String = "",
    val details: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class SystemConfig(
    val notificationsEnabled: Boolean = true,
    val maintenanceMode: Boolean = false,
    val appVersion: String = "1.0.0",
    val contactSupport: String = "support@barberapp.com"
)
