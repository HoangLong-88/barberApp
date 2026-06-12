package com.example.barberapp.Model.entities

import com.example.barberapp.View.screenUI.customer.home.Barber
import com.google.firebase.firestore.DocumentId

data class Shop (
    @DocumentId val id: String = "",
    val name: String ="",
    val address: String = "",
    val phone: String = "",
    val priceRange: String = "",
    val rating: Double = 0.0,
    val isFavorite: Boolean = false,
    val imageUrl: String ="",
    val services: List<Service> = emptyList(),
    val barbers: List<Barber> = emptyList()
)