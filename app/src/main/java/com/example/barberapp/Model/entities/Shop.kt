package com.example.barberapp.Model.entities

data class Shop (
    val id: Int = 0,
    val name: String ="",
    val address: String = "",
    val priceRange: String = "",
    val rating: Double = 0.0,
    val isFavorite: Boolean = false,
    val imageUrl: String ="",
)