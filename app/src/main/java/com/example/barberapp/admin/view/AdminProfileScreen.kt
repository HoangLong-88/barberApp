package com.example.barberapp.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // Cần thiết để dùng 'by'
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.admin.viewmodel.AdminViewModel

@Composable
fun AdminProfileScreen(viewModel: AdminViewModel) {
    // 1. Lấy dữ liệu Admin hiện tại từ ViewModel
    // 'by' giúp bạn dùng 'admin' như một biến bình thường thay vì admin.value
    val admin by viewModel.currentAdmin

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 2. Ảnh đại diện (Avatar)
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFF2C2C2C)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFFEBC14F), // Đổi sang màu vàng cho đồng bộ
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Hiển thị Tên và Email thật
        // Nếu admin null (đang tải), ta hiện "Đang tải..."
        Text(
            text = admin?.name ?: "Đang tải...",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = admin?.email ?: "vui lòng chờ...",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 4. Các mục Menu
        ProfileMenuItem("Thông tin cá nhân", Icons.Default.Edit)
        ProfileMenuItem("Đổi mật khẩu", Icons.Default.Lock)
        ProfileMenuItem("Cài đặt thông báo", Icons.Default.Notifications)

        Spacer(modifier = Modifier.weight(1f))

        // 5. Nút Đăng xuất
        Button(
            onClick = { viewModel.logout() }, // Gọi hàm logout đã viết trong ViewModel
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCF6679)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng xuất", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProfileMenuItem(title: String, icon: ImageVector) {
    Surface(
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        onClick = { /* Sau này sẽ code chuyển trang ở đây */ }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, color = Color.White, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}