package com.example.barberapp.Model.entities

data class Review(
    val id: String = "",
    val shopId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Long = 0L
)