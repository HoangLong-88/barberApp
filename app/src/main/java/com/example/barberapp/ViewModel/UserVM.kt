package com.example.barberapp.ViewModel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
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
                // Cập nhật cả trong Firestore để đồng bộ (nếu bạn lưu pass trong Firestore)
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
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String,
        newUri: Uri?,
        onDone: () -> Unit
    ) {
        val uid = userRepo.getCurrentUID() ?: return
        if (newUri != null && newUri.scheme != "https") {
            userRepo.uploadImage(uid, newUri) { downloadUrl ->
                val updatedUser = User(
                    id = uid,
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    role = role,
                    avatarUrl = downloadUrl ?: ""
                )
                userRepo.updateProfile(updatedUser) { success ->
                    if (success) fetchUserProfile()
                    onDone()
                }
            }
        } else {
            val currentUserData = User(uid, name, email, phone, password, role, userData?.avatarUrl)
            userRepo.updateProfile(currentUserData) {
                if (it) fetchUserProfile()
                onDone()
            }
        }
    }

    fun clearData() {
        userData = null
    }
}
