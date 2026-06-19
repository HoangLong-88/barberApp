package com.example.barberapp.Model.entities

data class BookingItem(
    val id: String = "",
    val customerName: String = "",
    val serviceName: String = "",
    val barberName: String = "",
    val dateTime: String = "",
    val status: String = "Pending"
)

