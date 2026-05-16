package com.example.barberapp.Model.entities

data class Staff(
    val id: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val rating: Double = 0.0,
    val totalRatings: Int = 0,
    val appointmentsToday: Int = 0,
    val completedToday: Int = 0
)