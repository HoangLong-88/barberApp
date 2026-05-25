package com.example.barberapp.Model.entities

import com.example.barberapp.View.screenUI.customer.bookings.BookingStatus

data class Booking(
    val id: String,
    val service: String,
    val shop: String,
    val dateTime: String,
    val barber: String,
    val price: String,
    val status: BookingStatus
)