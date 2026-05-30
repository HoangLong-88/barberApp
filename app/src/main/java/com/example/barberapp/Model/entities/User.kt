package com.example.barberapp.Model.entities

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.DocumentId
import androidx.core.graphics.toColorInt

data class User (
    @DocumentId val id : String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val role: String = "",
    val avatarUrl: String? = null,
    val roleColorHex: String = "#2196F3",
    val shopId: String = ""
){
    val roleColor: Color get() = try { Color(roleColorHex.toColorInt()) } catch (e: Exception) { Color(0xFF2196F3) }
}