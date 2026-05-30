package com.example.barberapp.View.screenUI.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.example.barberapp.View.component.DialogTextField

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
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
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
    var rating by remember { mutableDoubleStateOf(shop?.rating ?: 0.0) }
    var imageUrl by remember { mutableStateOf(shop?.imageUrl ?: "") }
    var dynamicService = remember {
        mutableStateListOf<Service>().apply { addAll(shop?.services ?: emptyList()) }
    }
    var dynamicBarber = remember {
        mutableStateListOf<Employee>().apply { addAll(shop?.barbers ?: emptyList()) }
    }


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E1E1E),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState())) {
                Text(
                    if (shop == null) "Thêm tiệm" else "Sửa tiệm",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))

                DialogTextField("Tên tiệm *", name, { name = it }, "King Barber Shop")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Địa chỉ *", address, { address = it }, "54 Nguyen Van Linh")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("SĐT", phone, { phone = it }, "0901234567")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Khoảng giá", priceRange, { priceRange = it }, "80k - 150k")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Image URL", imageUrl, { imageUrl = it }, "https://...")

                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                    ) { Text("Hủy") }
                    Button(
                        onClick = {
                            if (name.isNotBlank() && address.isNotBlank()) onConfirm(
                                name,
                                address,
                                phone,
                                priceRange,
                                rating,
                                imageUrl,
                                dynamicService,
                                dynamicBarber
                            )
                        },
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
                Text(
                    if (service == null) "Thêm dịch vụ" else "Sửa dịch vụ",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                DialogTextField("Tên dịch vụ", name, { name = it }, "Hair Cut")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Thời gian (phút)", duration, { duration = it }, "30")
                Spacer(modifier = Modifier.height(16.dp))
                DialogTextField("Giá (VND)", price, {
                    price = it}, "80.000")
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                    ) { Text("Hủy") }
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
    val password by remember { mutableStateOf(user?.password ?: "") }
    var role by remember { mutableStateOf(user?.role ?: "customer") }
    var shopId by remember { mutableStateOf(user?.shopId?:"") }
    var expandedRole by remember { mutableStateOf(false) }
    var expandedShop by remember { mutableStateOf(false) }
    val roles = listOf("customer" to "Khách hàng", "employee" to "Nhân viên", "manager" to "Quản lý")
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF1E1E1E)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    if (user == null) "Thêm tài khoản" else "Sửa tài khoản",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                DialogTextField("Họ tên", name, { name = it }, "Nguyen Van A")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField("Email", email, { email = it }, "email@example.com")
                Spacer(modifier = Modifier.height(12.dp))
                DialogTextField("Số điện thoại", phone, { phone = it }, "0901234567")
                Spacer(modifier = Modifier.height(12.dp))
                Text("Vai trò", color = Color.Gray, fontSize = 12.sp)
                Box {
                    Surface(
                        onClick = { expandedRole = true },
                        color = Color(0xFF2C2C2C),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(roles.find { it.first == role }?.second ?: "", color = Color.White)
                            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White)
                        }
                    }
                    DropdownMenu(
                        expanded = expandedRole,
                        onDismissRequest = { expandedRole = false },
                        modifier = Modifier.background(Color(0xFF2C2C2C))
                    ) {
                        roles.forEach { (k, v) ->
                            DropdownMenuItem(text = {
                                Text(
                                    v,
                                    color = Color.White
                                )
                            }, onClick = { role = k; expandedRole = false
                            if (role != "employee") shopId = ""})
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                // --- CHỌN NƠI LÀM VIỆC (CHỈ HIỂN THỊ NẾU LÀ NHÂN VIÊN) ---
                if (role == "employee") {
                    Text("Nơi làm việc", color = Color.Gray, fontSize = 12.sp)
                    Box {
                        Surface(
                            onClick = { expandedShop = true },
                            color = Color(0xFF2C2C2C),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Tìm tên shop hiện tại, nếu rỗng hiển thị "Chưa có"
                                val currentShopName = shops.find { it.id == shopId }?.name ?: "Chưa có"
                                Text(currentShopName, color = Color.White)
                                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White)
                            }
                        }
                        DropdownMenu(
                            expanded = expandedShop,
                            onDismissRequest = { expandedShop = false },
                            modifier = Modifier.background(Color(0xFF2C2C2C))
                        ) {
                            // Lựa chọn Chưa có
                            DropdownMenuItem(
                                text = { Text("Chưa có", color = Color.White) },
                                onClick = { shopId = ""; expandedShop = false }
                            )
                            // Load danh sách shop từ database
                            shops.forEach { shop ->
                                DropdownMenuItem(
                                    text = { Text(shop.name, color = Color.White) },
                                    onClick = { shopId = shop.id; expandedShop = false }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                    ) { Text("Hủy") }
                    Button(
                        onClick = {
                            if (name.isNotBlank()) onConfirm(
                                name,
                                email,
                                phone,
                                password,
                                role,
                                shopId
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F))
                    ) { Text(if (user == null) "Thêm" else "Lưu", color = Color.Black) }
                }
            }
        }
    }
}
