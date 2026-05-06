package com.example.barberapp.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.barberapp.admin.model.ServiceItem
import com.example.barberapp.admin.model.ShopItem
import com.example.barberapp.admin.model.UserItem

@Composable
fun ConfirmDeleteDialog(title: String, message: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Warning, null, tint = Color.Red, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text(message, color = Color.Gray, textAlign = TextAlign.Center, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(onClick = onConfirm, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Xóa") }
                }
            }
        }
    }
}

@Composable
fun AddEditShopDialog(shop: ShopItem?, onDismiss: () -> Unit, onConfirm: (String, String, String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf(shop?.name ?: "") }
    var address by remember { mutableStateOf(shop?.address ?: "") }
    var phone by remember { mutableStateOf(shop?.phone ?: "") }
    var priceRange by remember { mutableStateOf(shop?.priceRange ?: "") }
    var rating by remember { mutableStateOf(shop?.rating ?: "0") }
    var imageUrl by remember { mutableStateOf(shop?.imageUrl ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E), modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                Text(if(shop == null) "Thêm tiệm" else "Sửa tiệm", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                
                DialogTextField("Tên tiệm *", name, { name = it }, "King Barber Shop")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Địa chỉ *", address, { address = it }, "54 Nguyen Van Linh")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("SĐT", phone, { phone = it }, "0901234567")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Khoảng giá", priceRange, { priceRange = it }, "80k - 150k")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Rating", rating, { rating = it }, "0")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Image URL", imageUrl, { imageUrl = it }, "https://...")
                
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(onClick = { if(name.isNotBlank() && address.isNotBlank()) onConfirm(name, address, phone, priceRange, rating, imageUrl) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))) { Text(if(shop==null) "Thêm" else "Lưu", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun AddEditServiceDialog(service: ServiceItem?, onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf(service?.name ?: "") }
    var duration by remember { mutableStateOf(service?.duration ?: "") }
    var price by remember { mutableStateOf(service?.price ?: "") }
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(if(service == null) "Thêm dịch vụ" else "Sửa dịch vụ", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                DialogTextField("Tên dịch vụ", name, { name = it }, "Hair Cut")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Thời gian (phút)", duration, { duration = it }, "30")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Giá (VND)", price, { price = it }, "80.000")
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(onClick = { if(name.isNotBlank()) onConfirm(name, duration, price) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))) { Text(if(service==null) "Thêm" else "Lưu", color = Color.Black) }
                }
            }
        }
    }
}

@Composable
fun AddEditUserDialog(user: UserItem?, onDismiss: () -> Unit, onConfirm: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var role by remember { mutableStateOf(user?.role ?: "customer") }
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("customer" to "Khách hàng", "employee" to "Nhân viên", "manager" to "Quản lý")

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(if(user == null) "Thêm tài khoản" else "Sửa tài khoản", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                DialogTextField("Họ tên", name, { name = it }, "Nguyen Van A")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField("Email", email, { email = it }, "email@example.com")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField("Số điện thoại", phone, { phone = it }, "0901234567")
                Spacer(modifier = Modifier.height(12.dp))
                Text("Vai trò", color = Color.Gray, fontSize = 12.sp)
                Box {
                    Surface(onClick = { expanded = true }, color = Color(0xFF2C2C2C), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(roles.find { it.first == role }?.second ?: "", color = Color.White)
                            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White)
                        }
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color(0xFF2C2C2C))) {
                        roles.forEach { (k, v) -> DropdownMenuItem(text = { Text(v, color = Color.White) }, onClick = { role = k; expanded = false }) }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))) { Text("Hủy") }
                    Button(onClick = { if(name.isNotBlank()) onConfirm(name, email, phone, role) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))) { Text(if(user==null) "Thêm" else "Lưu", color = Color.Black) }
                }
            }
        }
    }
}
