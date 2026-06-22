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
        newPassword: String,
        confirmPassword: String,
        role: String,
        newUri: Uri?,
        onDone: () -> Unit,
    ) {
        val current = userData ?: return

        val isChangingPassword = confirmPassword.isNotEmpty() && newPassword == confirmPassword
        val passwordToSave = if (isChangingPassword) newPassword else current.password

        // ── Build updatedUser ──────────────────────────────────────────────────
        val updatedUser = if (newUri != null && newUri.scheme != "https") {
            val base64Avatar = uriToBase64(context, newUri) ?: ""
            current.copy(name = name, email = email, phone = phone, password = passwordToSave, role = role, avatarUrl = base64Avatar)
        } else {
            current.copy(name = name, email = email, phone = phone, password = passwordToSave, role = role)
        }

        // ── Nếu đổi password → update Firebase Auth trước, rồi mới Firestore ──
        if (isChangingPassword) {
            userRepo.updateAuthPassword(newPassword) { success, error ->
                if (success) {
                    userRepo.updateProfile(updatedUser) { dbSuccess ->
                        if (dbSuccess) fetchUserProfile()
                        onDone()
                    }
                } else {
                    // Auth thất bại → không update Firestore, báo lỗi
                    android.widget.Toast.makeText(
                        context,
                        "Đổi mật khẩu thất bại: $error",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                    onDone()
                }
            }
        } else {
            // Không đổi pass → update Firestore thẳng
            userRepo.updateProfile(updatedUser) { success ->
                if (success) fetchUserProfile()
                onDone()
            }
        }
    }

    // ── Overload cho admin (không có confirm password) ─────────────────────────
    fun saveChanges(
        context: Context,
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String,
        newUri: Uri?,
        onDone: () -> Unit,
    ) = saveChanges(
        context         = context,
        name            = name,
        email           = email,
        phone           = phone,
        newPassword     = password,
        confirmPassword = "",   // empty → isChangingPassword = false → giữ pass cũ
        role            = role,
        newUri          = newUri,
        onDone          = onDone
    )

    fun clearData() {
        userData = null
    }
}
