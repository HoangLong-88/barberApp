package com.example.barberapp.View.screenUI.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.barberapp.Model.BookingItem
import com.example.barberapp.Model.ServiceItem
import com.example.barberapp.Model.ShopItem
import com.example.barberapp.Model.UserItem
import com.example.barberapp.ViewModel.AdminViewModel

@Composable
fun AdminDashboardScreen(viewModel: AdminViewModel = viewModel()) {
    val currentTab by viewModel.currentTab
    val selectedUserFilter by viewModel.selectedUserFilter
    val selectedDateFilter by viewModel.selectedDateFilter
    val selectedShopForService by viewModel.selectedShopForService
    val searchQuery by viewModel.searchQuery
    
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            HeaderSection()
            Spacer(modifier = Modifier.height(20.dp))

            // Main Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item { TabButton("Thống kê", Icons.Default.Assessment, currentTab == "Thống kê") { viewModel.setCurrentTab("Thống kê") } }
                item { TabButton("Tiệm", Icons.Default.Home, currentTab == "Tiệm") { viewModel.setCurrentTab("Tiệm") } }
                item { TabButton("Tài khoản", Icons.Default.Person, currentTab == "Tài khoản") { viewModel.setCurrentTab("Tài khoản") } }
                item { TabButton("Dịch vụ", Icons.Default.Build, currentTab == "Dịch vụ") { viewModel.setCurrentTab("Dịch vụ") } }
                item { TabButton("Lịch booking", Icons.Default.DateRange, currentTab == "Lịch booking") { viewModel.setCurrentTab("Lịch booking") } }
                item { TabButton("Profile", Icons.Default.AccountCircle, currentTab == "Profile") { viewModel.setCurrentTab("Profile") } }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            if (currentTab != "Thống kê" && currentTab != "Profile" && (currentTab == "Tiệm" || currentTab == "Dịch vụ")) {
                SearchBarCustom(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sub-Header
            Column {
                if (currentTab == "Dịch vụ") {
                    Text("Chọn tiệm để quản lý dịch vụ:", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(viewModel.shops) { shop ->
                            FilterChipCustom(
                                label = shop.name,
                                isSelected = selectedShopForService?.id == shop.id,
                                onClick = { viewModel.setSelectedShopForService(shop) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    when (currentTab) {
                        "Tiệm" -> Text("Danh sách tiệm (${viewModel.shops.size})", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        "Tài khoản" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Tất cả", "KH", "NV", "QL").forEach { label ->
                                    FilterChipCustom(label, selectedUserFilter == label) { viewModel.setSelectedUserFilter(label) }
                                }
                            }
                        }
                        "Dịch vụ" -> Text("Dịch vụ tại: ${selectedShopForService?.name ?: "..."}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        "Lịch booking" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Tất cả", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                                    FilterChipCustom(day, selectedDateFilter == day) { viewModel.setSelectedDateFilter(day) }
                                }
                            }
                        }
                        "Thống kê" -> Text("Báo cáo kinh doanh", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    if (currentTab != "Lịch booking" && currentTab != "Thống kê" && currentTab != "Profile") {
                        IconButton(
                            onClick = {
                                when (currentTab) {
                                    "Tiệm" -> { viewModel.shopToEdit.value = null; viewModel.showAddShopDialog.value = true }
                                    "Tài khoản" -> { viewModel.userToEdit.value = null; viewModel.showAddUserDialog.value = true }
                                    "Dịch vụ" -> { viewModel.serviceToEdit.value = null; viewModel.showAddServiceDialog.value = true }
                                }
                            },
                            modifier = Modifier.size(36.dp).background(Color(0xFFEBC14F), RoundedCornerShape(8.dp))
                        ) { Icon(Icons.Default.Add, null, tint = Color.Black) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content List
            Box(modifier = Modifier.weight(1f)) {
                when (currentTab) {
                    "Tiệm" -> {
                        val filteredShops = viewModel.shops.filter { 
                            it.name.contains(searchQuery, ignoreCase = true) || it.address.contains(searchQuery, ignoreCase = true)
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filteredShops, key = { it.id }) { shop ->
                                ShopCard(shop, onEdit = { viewModel.shopToEdit.value = shop; viewModel.showAddShopDialog.value = true }, onDelete = { viewModel.itemToDelete.value = shop })
                            }
                        }
                    }
                    "Tài khoản" -> {
                        val filtered = if (selectedUserFilter == "Tất cả") viewModel.users 
                                      else viewModel.users.filter { 
                                          when(selectedUserFilter) { 
                                              "KH" -> it.role == "customer"
                                              "NV" -> it.role == "employee"
                                              "QL" -> it.role == "manager"
                                              else -> true 
                                          }
                                      }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { user ->
                                UserCard(user, onEdit = { viewModel.userToEdit.value = user; viewModel.showAddUserDialog.value = true }, onDelete = { viewModel.itemToDelete.value = user })
                            }
                        }
                    }
                    "Dịch vụ" -> {
                        val shopServices = viewModel.services.filter { 
                            it.shopId == selectedShopForService?.id && 
                            it.name.contains(searchQuery, ignoreCase = true)
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(shopServices, key = { it.id }) { service ->
                                ServiceCard(service, onEdit = { viewModel.serviceToEdit.value = service; viewModel.showAddServiceDialog.value = true }, onDelete = { viewModel.itemToDelete.value = service })
                            }
                        }
                    }
                    "Lịch booking" -> {
                        val filtered = if (selectedDateFilter == "Tất cả") viewModel.bookings 
                                      else viewModel.bookings.filter { it.dateTime.contains(selectedDateFilter) }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { booking ->
                                BookingCard(booking, onDelete = { viewModel.itemToDelete.value = booking })
                            }
                        }
                    }
                    "Thống kê" -> AdminStatisticsScreen(viewModel)
                    "Profile" -> AdminProfileScreen(viewModel)
                }
            }
        }
    }
    ViewDialogs(viewModel)
}

// --- Helper UI Components ---

@Composable
fun SearchBarCustom(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Tìm kiếm...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1E1E1E),
            unfocusedContainerColor = Color(0xFF1E1E1E),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun FilterChipCustom(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) Color(0xFFEBC14F) else Color(0xFF1E1E1E),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.Black else Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ShopCard(shop: ShopItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = shop.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(shop.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(shop.address, color = Color.Gray, fontSize = 12.sp, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(14.dp))
                    Text(" ${shop.rating}", color = Color.White, fontSize = 12.sp)
                }
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = Color.Gray) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color(0xFFCF6679)) }
        }
    }
}

@Composable
fun UserCard(user: UserItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(user.roleColor.copy(0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = user.roleColor)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(user.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(user.email, color = Color.Gray, fontSize = 12.sp)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = Color.Gray) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color(0xFFCF6679)) }
        }
    }
}

@Composable
fun ServiceCard(service: ServiceItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(service.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text("${service.duration} • ${service.price}", color = Color(0xFFEBC14F), fontSize = 13.sp)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = Color.Gray) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color(0xFFCF6679)) }
        }
    }
}

@Composable
fun BookingCard(booking: BookingItem, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(booking.customerName, color = Color.White, fontWeight = FontWeight.Bold)
            Text("${booking.serviceName} - ${booking.barberName}", color = Color.Gray, fontSize = 13.sp)
            Text(booking.dateTime, color = Color.Gray, fontSize = 13.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color(0xFFCF6679)) }
            }
        }
    }
}

// --- Dialogs ---

@Composable
fun ViewDialogs(viewModel: AdminViewModel) {
    val itemToDelete by viewModel.itemToDelete
    if (viewModel.showAddUserDialog.value) AddEditUserDialog(viewModel.userToEdit.value, onDismiss = { viewModel.showAddUserDialog.value = false }, onSave = { n, e, p, r -> viewModel.saveUser(n, e, p, r) })
    if (viewModel.showAddServiceDialog.value) AddEditServiceDialog(viewModel.serviceToEdit.value, onDismiss = { viewModel.showAddServiceDialog.value = false }, onSave = { n, d, p -> viewModel.saveService(n, d, p) })
    if (viewModel.showAddShopDialog.value) AddEditShopDialog(viewModel.shopToEdit.value, onDismiss = { viewModel.showAddShopDialog.value = false }, onSave = { n, a, p, pr, r, i -> viewModel.saveShop(n, a, p, pr, r, i) })
    itemToDelete?.let { item -> ConfirmDeleteDialog(onDismiss = { viewModel.itemToDelete.value = null }, onConfirm = { viewModel.deleteItem(item) }) }
}

@Composable
fun ConfirmDeleteDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Xác nhận xóa?") },
        text = { Text("Hành động này không thể hoàn tác.") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Xóa", color = Color.Red) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
fun AddEditUserDialog(user: UserItem?, onDismiss: () -> Unit, onSave: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var role by remember { mutableStateOf(user?.role ?: "customer") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Thêm tài khoản" else "Sửa tài khoản") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("SĐT") })
            }
        },
        confirmButton = { Button(onClick = { onSave(name, email, phone, role) }) { Text("Lưu") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
fun AddEditServiceDialog(service: ServiceItem?, onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf(service?.name ?: "") }
    var duration by remember { mutableStateOf(service?.duration ?: "") }
    var price by remember { mutableStateOf(service?.price ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (service == null) "Thêm dịch vụ" else "Sửa dịch vụ") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên dịch vụ") })
                OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Thời gian") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Giá") })
            }
        },
        confirmButton = { Button(onClick = { onSave(name, duration, price) }) { Text("Lưu") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
fun AddEditShopDialog(shop: ShopItem?, onDismiss: () -> Unit, onSave: (String, String, String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf(shop?.name ?: "") }
    var address by remember { mutableStateOf(shop?.address ?: "") }
    var phone by remember { mutableStateOf(shop?.phone ?: "") }
    var priceRange by remember { mutableStateOf(shop?.priceRange ?: "") }
    var rating by remember { mutableStateOf(shop?.rating ?: "") }
    var imageUrl by remember { mutableStateOf(shop?.imageUrl ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (shop == null) "Thêm tiệm" else "Sửa tiệm") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên tiệm") })
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Địa chỉ") })
            }
        },
        confirmButton = { Button(onClick = { onSave(name, address, phone, priceRange, rating, imageUrl) }) { Text("Lưu") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
fun HeaderSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Admin Dashboard", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Quản lý hệ thống", color = Color.Gray, fontSize = 14.sp)
        }
        IconButton(onClick = { }) { Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = Color.White) }
    }
}

@Composable
fun TabButton(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) Color(0xFFEBC14F) else Color(0xFF2C2C2C),
        shape = RoundedCornerShape(25.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isSelected) Color.Black else Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = if (isSelected) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
