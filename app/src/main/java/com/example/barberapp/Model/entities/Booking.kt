package com.example.barberapp.Model.entities

data class Booking(
    val service: String,
    val shop: String,
    val time: String,
    val barber: String,
    val price: String,
    val status: String
)