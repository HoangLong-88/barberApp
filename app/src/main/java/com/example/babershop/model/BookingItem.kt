package com.example.babershop.model

data class BookingItem(
    val service: String,
    val shop: String,
    val time: String,
    val barber: String,
    val price: String,
    val status: String
)