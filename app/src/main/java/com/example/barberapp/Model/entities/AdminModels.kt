package com.example.barberapp.Model.entities

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

