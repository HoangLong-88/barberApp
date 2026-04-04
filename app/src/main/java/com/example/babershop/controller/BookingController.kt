package com.example.babershop.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.babershop.model.BookingItem

class BookingController {
    var selectedFilter by mutableStateOf("All")

    // Sau này dữ liệu này sẽ lấy từ Backend/Firebase
    val allBookings = listOf(
        BookingItem("Hair Cut","King Barber Shop","20 May - 17:00","John","80.000 VND","Completed"),
        BookingItem("Beard Shave","King Barber Shop","18 May - 14:00","Mike","40.000 VND","Completed"),
        BookingItem("Hair Styling","Elite Cuts Studio","25 May - 10:00","David","150.000 VND","Pending"),
        BookingItem("Hair Cut","Classic Barber","28 May - 09:00","John","70.000 VND","Cancelled")
    )

    fun getFilteredBookings(): List<BookingItem> {
        return when(selectedFilter) {
            "Completed" -> allBookings.filter { it.status == "Completed" }
            "Pending" -> allBookings.filter { it.status == "Pending" }
            "Cancelled" -> allBookings.filter { it.status == "Cancelled" }
            else -> allBookings
        }
    }
}