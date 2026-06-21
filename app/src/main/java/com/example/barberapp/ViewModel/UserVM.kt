package com.example.barberapp.ViewModel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.example.barberapp.Helps.uriToBase64
import com.example.barberapp.Model.entities.User
import com.example.barberapp.Repository.UserRepository

class UserVM : ViewModel() {
    private val userRepo = UserRepository()
    var userData by mutableStateOf<User?>(null)
        private set

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        val uid = userRepo.getCurrentUID() ?: return
        userRepo.getUserData(uid) { user, error ->
            if (user != null) {
                userData = user
            }
        }
    }

    fun updatePassword(newPass: String, onDone: (Boolean, String?) -> Unit) {
        userRepo.updateAuthPassword(newPass) { success, error ->
            if (success) {
                val current = userData ?: return@updateAuthPassword
                val updated = current.copy(password = newPass)
                userRepo.updateProfile(updated) { dbSuccess ->
                    if (dbSuccess) fetchUserProfile()
                    onDone(dbSuccess, if (dbSuccess) null else "Lỗi cập nhật Firestore")
                }
            } else {
                onDone(false, error)
            }
        }
    }

    fun saveChanges(
        context: Context,
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String,
        newUri: Uri?,
        onDone: () -> Unit,
    ) {
        val uid = userRepo.getCurrentUID() ?: return
        val current = userData ?: return

        if (newUri != null && newUri.scheme != "https") {
            // Theo logic đồng nghiệp: Chuyển ảnh sang Base64
            val base64Avatar = uriToBase64(context, newUri) ?: ""
            val updatedUser = current.copy(
                name = name,
                email = email,
                phone = phone,
                password = password,
                role = role,
                avatarUrl = base64Avatar
            )
            userRepo.updateProfile(updatedUser) { success ->
                if (success) fetchUserProfile()
                onDone()
            }
        } else {
            // Nếu không thay ảnh, chỉ cập nhật các textfield
            val updatedUser = current.copy(
                name = name,
                email = email,
                phone = phone,
                password = password,
                role = role
            )
            userRepo.updateProfile(updatedUser) { success ->
                if (success) fetchUserProfile()
                onDone()
            }
        }
    }

    fun clearData() {
        userData = null
    }
}
