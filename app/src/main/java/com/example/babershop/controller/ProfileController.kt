package com.example.babershop.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.babershop.model.User

class ProfileController {
    // Khởi tạo user với dữ liệu mẫu ban đầu
    var user by mutableStateOf(
        User(
            name = "Tran Huan",
            email = "huan123@gmail.com",
            phone = "090xxxxxxx",
            bookingsCount = "12",
            reviewsCount = "5",
            points = "320"
        )
    )

    // Hàm cập nhật thông tin (sẽ dùng trong EditProfileScreen)
    fun updateUserInfo(newName: String, newEmail: String, newPhone: String) {
        user = user.copy(
            name = newName,
            email = newEmail,
            phone = newPhone
        )
        // Sau này sẽ thêm code cập nhật lên Firebase tại đây
    }
}