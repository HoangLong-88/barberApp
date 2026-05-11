package com.example.barberapp.Repository.auth

import com.example.barberapp.Helper.generateUID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    fun checkLogin(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) onResult(true, null)
            else onResult(false, task.exception?.message)
        }
    }

    fun checkSignUp(
        username: String,
        email: String,
        phone: String,
        password: String,
        role: String = "customer",
        profileImgUrl: String?,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userID = auth.currentUser?.uid?:""
                val userMap = hashMapOf(
                    "name" to username,
                    "email" to email,
                    "phone" to phone,
                    "password" to password,
                    "role" to role,
                    "profileImgUrl" to profileImgUrl,
                    "createAt" to System.currentTimeMillis()
                )
                db.collection("users").document(userID).set(userMap)
                    .addOnSuccessListener { onResult(true, null) }
                    .addOnFailureListener { exception -> onResult(false, exception.message) }
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }
    fun getLogOut(): Unit = auth.signOut()
}