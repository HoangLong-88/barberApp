package com.example.barberapp.View.screenUI.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.barberapp.Model.entities.AuditLog
import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.SystemConfig
import com.example.barberapp.Model.entities.User
import com.example.barberapp.View.component.DialogTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AuditLogDialog(
    logs: List<AuditLog>,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val sdf = remember(configuration) { 
        SimpleDateFormat("HH:mm dd/MM/yyyy", configuration.locales[0]) 
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E1E1E),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Lịch sử hoạt động",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.History, null, tint = Color(0xFFEBC14F))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (logs.isEmpty()) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text("Chưa có hoạt động nào", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(logs) { log ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            log.action,
                                            color = Color(0xFFEBC14F),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            sdf.format(Date(log.timestamp)),
                                            color = Color.Gray,
                                            fontSize = 10.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(log.details, color = Color.White, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        "Thực hiện bởi: ${log.adminName}",
                                        color = Color.DarkGray,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                ) {
                    Text("Đóng")
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Warning, null, tint = Color.Red, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text(message, color = Color.Gray, textAlign = TextAlign.Center, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                    ) { Text("Hủy") }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text("Xóa") }
                }
            }
        }
    }
}

@Composable
fun AddEditShopDialog(
    shop: Shop?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Double, String, List<Service>, List<Employee>) -> Unit
) {
    var name by remember { mutableStateOf(shop?.name ?: "") }
    var address by remember { mutableStateOf(shop?.address ?: "") }
    var phone by remember { mutableStateOf(shop?.phone ?: "") }
    var priceRange by remember { mutableStateOf(shop?.priceRange ?: "") }
    var imageUrl by remember { mutableStateOf(shop?.imageUrl ?: "") }
    val dynamicService = remember { mutableStateListOf<Service>().apply { addAll(shop?.services ?: emptyList()) } }
    val dynamicBarber = remember { mutableStateListOf<Employee>().apply { addAll(shop?.barbers ?: emptyList()) } }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E1E1E),
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                Text(if (shop == null) "Thêm tiệm mới" else "Chỉnh sửa tiệm", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                DialogTextField("Tên tiệm *", name, { name = it }, "King Barber Shop")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Địa chỉ *", address, { address = it }, "54 Nguyễn Văn Linh")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Số điện thoại", phone, { phone = it }, "0901234567")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Khoảng giá", priceRange, { priceRange = it }, "80k - 150k")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Link ảnh", imageUrl, { imageUrl = it }, "https://...")
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(
                        onClick = { if (name.isNotBlank() && address.isNotBlank()) onConfirm(name, address, phone, priceRange, shop?.rating ?: 5.0, imageUrl, dynamicService, dynamicBarber) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))
                    ) { Text(if (shop == null) "Thêm" else "Lưu", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun AddEditServiceDialog(
    service: Service?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(service?.name ?: "") }
    var duration by remember { mutableStateOf(service?.duration ?: "") }
    var price by remember { mutableStateOf(if (service != null && service.price > 0) service.price.toString() else "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(if (service == null) "Thêm dịch vụ" else "Sửa dịch vụ", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                DialogTextField("Tên dịch vụ", name, { name = it }, "Cắt tóc nam")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Thời gian (phút)", duration, { duration = it }, "30")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Giá (VND)", price, { price = it }, "80.000")
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(
                        onClick = { if (name.isNotBlank()) onConfirm(name, duration, price) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))
                    ) { Text(if (service == null) "Thêm" else "Lưu", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun AddEditUserDialog(
    user: User?,
    shops: List<Shop>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var password by remember { mutableStateOf(user?.password ?: "") }
    var role by remember { mutableStateOf(user?.role ?: "customer") }
    var shopId by remember { mutableStateOf(user?.shopId ?: "") }
    var expandedRole by remember { mutableStateOf(false) }
    var expandedShop by remember { mutableStateOf(false) }
    val roles = listOf("customer" to "Khách hàng", "employee" to "Nhân viên", "manager" to "Quản lý")

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E), modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                Text(if (user == null) "Thêm tài khoản" else "Sửa tài khoản", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                DialogTextField("Họ tên", name, { name = it }, "Nguyễn Văn A")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField("Email", email, { email = it }, "email@example.com")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField("Số điện thoại", phone, { phone = it }, "0901234567")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField("Mật khẩu", password, { password = it }, "********")
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Vai trò", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
                Box {
                    Surface(onClick = { expandedRole = true }, color = Color(0xFF2C2C2C), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(roles.find { it.first == role }?.second ?: "", color = Color.White)
                            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White)
                        }
                    }
                    DropdownMenu(expanded = expandedRole, onDismissRequest = { expandedRole = false }, modifier = Modifier.background(Color(0xFF2C2C2C))) {
                        roles.forEach { (k, v) ->
                            DropdownMenuItem(text = { Text(v, color = Color.White) }, onClick = { role = k; expandedRole = false; if (role != "employee") shopId = "" })
                        }
                    }
                }

                if (role == "employee") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Nơi làm việc", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
                    Box {
                        Surface(onClick = { expandedShop = true }, color = Color(0xFF2C2C2C), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(shops.find { it.id == shopId }?.name ?: "Chưa chọn", color = Color.White)
                                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White)
                            }
                        }
                        DropdownMenu(expanded = expandedShop, onDismissRequest = { expandedShop = false }, modifier = Modifier.background(Color(0xFF2C2C2C))) {
                            DropdownMenuItem(text = { Text("Chưa có", color = Color.White) }, onClick = { shopId = ""; expandedShop = false })
                            shops.forEach { shop ->
                                DropdownMenuItem(text = { Text(shop.name, color = Color.White) }, onClick = { shopId = shop.id; expandedShop = false })
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(
                        onClick = { if (name.isNotBlank()) onConfirm(name, email, phone, password, role, shopId) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))
                    ) { Text(if (user == null) "Thêm" else "Lưu", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun EditProfileDialog(
    user: User,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Uri?) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var phone by remember { mutableStateOf(user.phone) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                Text("Chỉnh sửa hồ sơ", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                
                // Avatar Picker
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(100.dp)) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = CircleShape,
                            color = Color(0xFF2C2C2C)
                        ) {
                            if (imageUri != null) {
                                AsyncImage(model = imageUri, contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                            } else if (!user.avatarUrl.isNullOrEmpty()) {
                                AsyncImage(model = user.avatarUrl, contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                            } else {
                                Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.padding(20.dp))
                            }
                        }
                        IconButton(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier.align(Alignment.BottomEnd).size(32.dp).background(Color(0xFFEBC14F), CircleShape)
                        ) {
                            Icon(Icons.Default.CameraAlt, null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                DialogTextField("Họ và tên", name, { name = it }, "")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Số điện thoại", phone, { phone = it }, "")
                
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(
                        onClick = { onConfirm(name, user.email, phone, imageUri) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))
                    ) { Text("Lưu thay đổi", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Đổi mật khẩu", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                
                DialogTextField("Mật khẩu mới", newPassword, { newPassword = it; error = null }, "********")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Xác nhận mật khẩu", confirmPassword, { confirmPassword = it; error = null }, "********")
                
                if (error != null) {
                    Text(error!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(
                        onClick = {
                            if (newPassword.length < 6) error = "Mật khẩu phải từ 6 ký tự"
                            else if (newPassword != confirmPassword) error = "Mật khẩu xác nhận không khớp"
                            else onConfirm(newPassword)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))
                    ) { Text("Đổi mật khẩu", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun SystemSettingsDialog(
    config: SystemConfig,
    onDismiss: () -> Unit,
    onConfirm: (SystemConfig) -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(config.notificationsEnabled) }
    var maintenanceMode by remember { mutableStateOf(config.maintenanceMode) }
    var contactSupport by remember { mutableStateOf(config.contactSupport) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, null, tint = Color(0xFFEBC14F))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Cài đặt hệ thống", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Notifications Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Thông báo Booking", color = Color.White, fontWeight = FontWeight.Medium)
                        Text("Nhận thông báo khi có lịch hẹn mới", color = Color.Gray, fontSize = 12.sp)
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFEBC14F))
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Maintenance Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Chế độ bảo trì", color = Color.White, fontWeight = FontWeight.Medium)
                        Text("Tạm dừng nhận lịch hẹn trên toàn hệ thống", color = Color.Gray, fontSize = 12.sp)
                    }
                    Switch(
                        checked = maintenanceMode,
                        onCheckedChange = { maintenanceMode = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Red)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                DialogTextField("Email hỗ trợ", contactSupport, { contactSupport = it }, "support@barberapp.com")
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                    ) { Text("Hủy") }
                    Button(
                        onClick = { 
                            onConfirm(config.copy(
                                notificationsEnabled = notificationsEnabled,
                                maintenanceMode = maintenanceMode,
                                contactSupport = contactSupport
                            ))
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))
                    ) { Text("Cập nhật", color = Color.Black) }
                }
            }
        }
    }
}
