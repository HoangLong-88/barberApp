package com.example.barberapp.Model.entities

data class Employee(
    val id: String = "",
    val shopId: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val rating: Double = 0.0,
    val totalRatings: Int = 0,
    val appointmentsToday: Int = 0,
    val completedToday: Int = 0
)