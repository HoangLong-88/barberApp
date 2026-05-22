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

    fun fetchUserProfile() {
        val uid = userRepo.getCurrentUID() ?: return
        userRepo.getUserData(uid) { user, error ->
            if (user != null) {
                userData = user // This will trigger a recomposition
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
        // Nếu có ảnh mới (uri không phải link web https)
        if (newUri != null && newUri.scheme != "https") {
            // 1. Upload lên Storage trước
            userRepo.uploadImage(uid, newUri) { downloadUrl ->
                // 2. Sau khi có Link ảnh, tạo object User mới
                val updatedUser = User(
                    id = uid,
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    role = role,
                    avatarUrl = downloadUrl ?: ""
                )
                // 3. Lưu object User này vào Firestore
                userRepo.updateProfile(updatedUser) { success ->
                    if (success) fetchUserProfile() // Tải lại dữ liệu mới
                    onDone()
                }
            }
        } else {
            // Nếu không thay ảnh, chỉ cập nhật text
            val currentUserData =
                User(uid, name, email, phone, password, role, userData?.avatarUrl)
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