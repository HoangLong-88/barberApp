package com.example.barberapp.Repository

import android.net.Uri
import com.example.barberapp.Model.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserRepository {
    private val store = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    fun getCurrentUID(): String? = auth.currentUser?.uid
    fun getUserData(uid: String, onResult: (User?, String?) -> Unit) {
        store.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    onResult(user, null)
                } else {
                    onResult(null, "User data not found")
                }
            }
            .addOnFailureListener { e ->
                onResult(null, e.message)
            }
    }
    fun updateProfile(user: User, onComplete: (Boolean) -> Unit) {
        store.collection("users").document(user.id)
            .set(user)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }
}