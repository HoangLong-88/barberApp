package com.example.barberapp.Model.entities

import com.google.firebase.firestore.DocumentId

data class User (
    @DocumentId val id : String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val role: String = "",
    val avatarUrl: String? = null,
)