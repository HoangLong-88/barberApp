package com.example.barberapp.Repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val store = FirebaseFirestore.getInstance()
    fun checkLogin(email: String, password: String, onResult: (Boolean, String?, String?) -> Unit) {
        val emailTrim = email.trim()
        val passTrim = password
        if (emailTrim.isEmpty() || passTrim.isEmpty()) {
            onResult(false, "Email hoặc mật khẩu không được để trống", null)
            return
        }

        auth.signInWithEmailAndPassword(emailTrim, passTrim).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    store.collection("users").document(uid)
                        .get().addOnSuccessListener { document ->
                            val role = document.getString("role") ?: "customer"
                            onResult(true, null, role)
                        }.addOnFailureListener { exception ->
                            Log.e("AuthRepository", "Failed to fetch user role", exception)
                            onResult(false, "Không thể lấy thông tin vai trò", null)
                        }
                } else {
                    onResult(false, "Đăng nhập thất bại: không lấy được uid", null)
                }
            } else {
                val ex = task.exception
                Log.w("AuthRepository", "signIn failed", ex)
                val friendly = when (ex) {
                    is FirebaseAuthInvalidCredentialsException -> "Mật khẩu hoặc thông tin xác thực không đúng"
                    is FirebaseAuthInvalidUserException -> "Tài khoản không tồn tại hoặc đã bị vô hiệu hóa"
                    is FirebaseAuthException -> "Lỗi xác thực: ${ex.errorCode}"
                    else -> ex?.message ?: "Đăng nhập thất bại"
                }
                onResult(false, friendly, null)
            }
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