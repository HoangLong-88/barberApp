package com.example.barberapp.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val store = FirebaseFirestore.getInstance()
    fun checkLogin(email: String, password: String, onResult: (Boolean, String?, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val uid = auth.currentUser?.uid
                if (uid != null){
                    store.collection("users").document(uid)
                        .get().addOnSuccessListener { document->
                            val role= document.getString("role")?:"customer"
                            onResult(true,null,role)
                        }.addOnFailureListener {
                            onResult(false,"Cannot get role info!",null)
                        }
                }
            }
            else onResult(false, task.exception?.message,null)
        }
    }

    fun checkSignUp(
        username: String,
        email: String,
        phone: String,
        password: String,
        role: String = "customer",
        avatarUrl: String?,
        shopId: String,
        roleColorHex: String,
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
                    "avatarUrl" to avatarUrl,
                    "shopId" to shopId,
                    "roleColorHex" to roleColorHex,
                    "createAt" to System.currentTimeMillis()
                )
                store.collection("users").document(userID).set(userMap)
                    .addOnSuccessListener { onResult(true, null) }
                    .addOnFailureListener { exception -> onResult(false, exception.message) }
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }
    fun getLogOut(): Unit = auth.signOut()
}