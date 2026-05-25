package com.example.barberapp.Model.entities

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class Shop (
    @DocumentId val id: String = "",
    val name: String ="",
    val address: String = "",
    val phone: String = "",
    val priceRange: String = "",
    val rating: Double = 0.0,
    val isFavorite: Boolean = false,
    val imageUrl: String ="",
    @get:Exclude
    var services: List<Service> = emptyList(),
    @get:Exclude
    var barbers: List<Employee> = emptyList()
)