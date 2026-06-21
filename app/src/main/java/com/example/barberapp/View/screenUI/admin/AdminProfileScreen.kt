package com.example.barberapp.View.screenUI.admin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.barberapp.Helps.decodeBase64ToBitmap
import com.example.barberapp.ViewModel.AdminViewModel
import com.example.barberapp.ViewModel.AuthVM
import com.example.barberapp.ViewModel.ShopVM
import com.example.barberapp.ViewModel.UserVM

@Composable
fun AdminProfileScreen(
    authVM: AuthVM,
    userVM: UserVM,
    shopVM: ShopVM
) {
    val admin = userVM.userData
    val context = LocalContext.current
    val adminVM: AdminViewModel = viewModel()
    
    // States cho các Dialog
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showAuditLogDialog by remember { mutableStateOf(false) }
    var showSystemSettingsDialog by remember { mutableStateOf(false) } 

    val systemConfig by adminVM.systemConfig 

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // Avatar Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable { showEditProfileDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                val avatarData = admin?.avatarUrl
                if (!avatarData.isNullOrEmpty()) {
                    // KIỂM TRA NẾU LÀ BASE64 THÌ GIẢI MÃ (Theo logic đồng nghiệp)
                    if (avatarData.startsWith("data:image") || avatarData.length > 100) {
                        val bitmap = decodeBase64ToBitmap(avatarData)
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Nếu giải mã lỗi, dùng icon mặc định
                            Icon(Icons.Default.Person, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(70.dp))
                        }
                    } else {
                        // Nếu vẫn là URL cũ (Firebase Storage)
                        AsyncImage(
                            model = avatarData,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFFEBC14F),
                        modifier = Modifier.size(70.dp)
                    )
                }
            }
            // Camera Edit Icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEBC14F))
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .clickable { showEditProfileDialog = true }
            ) {
                Icon(Icons.Default.CameraAlt, null, tint = Color.Black, modifier = Modifier.fillMaxSize())
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Name & Email
        Text(
            text = admin?.name ?: "Admin",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = admin?.email ?: "admin@barber.com",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        
        // Role Badge
        Surface(
            color = Color(0xFFEBC14F).copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "ADMINISTRATOR",
                color = Color(0xFFEBC14F),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Menu Items
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ProfileMenuItem(
                title = "Thông tin tài khoản",
                subtitle = "${admin?.name ?: "N/A"}, ${admin?.phone ?: "Chưa có SĐT"}",
                icon = Icons.Default.Info,
                onClick = { showEditProfileDialog = true }
            )
            ProfileMenuItem(
                title = "Lịch sử hoạt động",
                subtitle = "Theo dõi các thay đổi hệ thống",
                icon = Icons.Default.History,
                onClick = { showAuditLogDialog = true }
            )
            ProfileMenuItem(
                title = "Đổi mật khẩu",
                subtitle = "Thay đổi mật khẩu đăng nhập",
                icon = Icons.Default.Lock,
                onClick = { showChangePasswordDialog = true }
            )
            ProfileMenuItem(
                title = "Cài đặt hệ thống",
                subtitle = "Thông báo: ${if(systemConfig.notificationsEnabled) "Bật" else "Tắt"}, Bảo trì: ${if(systemConfig.maintenanceMode) "Bật" else "Tắt"}",
                icon = Icons.Default.Settings,
                onClick = { 
                    showSystemSettingsDialog = true 
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Logout Button
        Button(
            onClick = { authVM.logOut(userVM, shopVM) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCF6679).copy(alpha = 0.15f)),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCF6679).copy(alpha = 0.3f))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color(0xFFCF6679))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Đăng xuất khỏi hệ thống", color = Color(0xFFCF6679), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Version Info
        Text(
            text = "Version ${systemConfig.appVersion} - Powered by BarberApp",
            color = Color.DarkGray,
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }

    // --- HIỂN THỊ CÁC DIALOG ---

    if (showSystemSettingsDialog) {
        SystemSettingsDialog(
            config = systemConfig,
            onDismiss = { showSystemSettingsDialog = false },
            onConfirm = { newConfig ->
                adminVM.updateSystemConfig(newConfig)
                showSystemSettingsDialog = false
                Toast.makeText(context, "Đã cập nhật cấu hình hệ thống", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showAuditLogDialog) {
        AuditLogDialog(
            logs = adminVM.auditLogs,
            onDismiss = { showAuditLogDialog = false }
        )
    }

    if (showEditProfileDialog && admin != null) {
        EditProfileDialog(
            user = admin,
            onDismiss = { showEditProfileDialog = false },
            onConfirm = { name, email, phone, uri ->
                userVM.saveChanges(context, name, email, phone, admin.password, admin.role, uri) {
                    showEditProfileDialog = false
                    Toast.makeText(context, "Đã cập nhật hồ sơ", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onConfirm = { newPass ->
                userVM.updatePassword(newPass) { success, error ->
                    if (success) {
                        showChangePasswordDialog = false
                        Toast.makeText(context, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, error ?: "Lỗi không xác định", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF121212), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
        }
    }
}
