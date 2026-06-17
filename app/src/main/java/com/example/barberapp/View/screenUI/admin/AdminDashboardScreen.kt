package com.example.barberapp.View.screenUI.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import com.example.barberapp.ViewModel.AdminViewModel
import com.example.barberapp.ViewModel.AuthVM
import com.example.barberapp.ViewModel.UserVM
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User

@Composable
fun AdminDashboardScreen(navController: NavController, authVM: AuthVM, userVM: UserVM) {
    val viewModel: AdminViewModel = viewModel()
    
    // Read state from ViewModel
    val currentTab by viewModel.currentTab
    val selectedUserFilter by viewModel.selectedUserFilter
    val selectedDateFilter by viewModel.selectedDateFilter
    val selectedShopForService by viewModel.selectedShopForService
    val searchQuery by viewModel.searchQuery
    
    val shops = viewModel.shops
    val users = viewModel.users
    val services = viewModel.services
    val bookings = viewModel.bookings
    
    val showAddUserDialog by viewModel.showAddUserDialog
    val showAddServiceDialog by viewModel.showAddServiceDialog
    val showAddShopDialog by viewModel.showAddShopDialog
    val userToEdit by viewModel.userToEdit
    val serviceToEdit by viewModel.serviceToEdit
    val shopToEdit by viewModel.shopToEdit
    val itemToDelete by viewModel.itemToDelete

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            HeaderSection(onLogout = { viewModel.logout(); navController.navigate("login") })
            Spacer(modifier = Modifier.height(20.dp))

            // Main Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item { TabButton("Thống kê", Icons.Default.Assessment, currentTab == "Thống kê") { viewModel.setCurrentTab("Thống kê") } }
                item { TabButton("Tiệm", Icons.Default.Home, currentTab == "Tiệm") { viewModel.setCurrentTab("Tiệm") } }
                item { TabButton("Tài khoản", Icons.Default.Person, currentTab == "Tài khoản") { viewModel.setCurrentTab("Tài khoản") } }
                item { TabButton("Dịch vụ", Icons.Default.Build, currentTab == "Dịch vụ") { viewModel.setCurrentTab("Dịch vụ") } }
                item { TabButton("Lịch booking", Icons.Default.DateRange, currentTab == "Lịch booking") { viewModel.setCurrentTab("Lịch booking") } }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (currentTab == "Tiệm" || currentTab == "Dịch vụ") {
                SearchBarCustom(query = searchQuery, onQueryChange = { viewModel.setSearchQuery(it) })
                Spacer(modifier = Modifier.height(8.dp))
            }

            Column {
                if (currentTab == "Dịch vụ") {
                    Text("Chọn tiệm để quản lý dịch vụ:", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(shops) { shop ->
                            FilterChipCustom(label = shop.name, isSelected = selectedShopForService?.id == shop.id) { viewModel.setSelectedShopForService(shop) }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    when (currentTab) {
                        "Tiệm" -> Text("Danh sách tiệm (${shops.size})", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        "Tài khoản" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Tất cả", "KH", "NV", "QL").forEach { label ->
                                    FilterChipCustom(label = label, isSelected = selectedUserFilter == label) { viewModel.setSelectedUserFilter(label) }
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

                    if (currentTab != "Lịch booking" && currentTab != "Thống kê") {
                        IconButton(onClick = {
                            when (currentTab) {
                                "Tiệm" -> { viewModel.shopToEdit.value = null; viewModel.showAddShopDialog.value = true }
                                "Tài khoản" -> { viewModel.userToEdit.value = null; viewModel.showAddUserDialog.value = true }
                                "Dịch vụ" -> { viewModel.serviceToEdit.value = null; viewModel.showAddServiceDialog.value = true }
                            }
                        }, modifier = Modifier.size(36.dp).background(Color(0xFFEBC14F), RoundedCornerShape(8.dp))) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                when (currentTab) {
                    "Tiệm" -> {
                        val filteredShops = shops.filter { it.name.contains(searchQuery, ignoreCase = true) || it.address.contains(searchQuery, ignoreCase = true) }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filteredShops, key = { it.id }) { shop ->
                                ShopCard(shop, onEdit = { viewModel.shopToEdit.value = shop; viewModel.showAddShopDialog.value = true }, onDelete = { viewModel.itemToDelete.value = shop })
                            }
                        }
                    }
                    "Tài khoản" -> {
                        val filtered = if (selectedUserFilter == "Tất cả") users else users.filter {
                            when (selectedUserFilter) {
                                "KH" -> it.role == "customer"
                                "NV" -> it.role == "employee"
                                "QL" -> it.role == "manager"
                                else -> true
                            }
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { user -> UserCard(user, onEdit = { viewModel.userToEdit.value = user; viewModel.showAddUserDialog.value = true }, onDelete = { viewModel.itemToDelete.value = user }) }
                        }
                    }
                    "Dịch vụ" -> {
                        val shopServices = services.filter { it.shopId == selectedShopForService?.id && it.name.contains(searchQuery, ignoreCase = true) }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(shopServices, key = { it.id }) { service -> ServiceCard(service, onEdit = { viewModel.serviceToEdit.value = service; viewModel.showAddServiceDialog.value = true }, onDelete = { viewModel.itemToDelete.value = service }) }
                        }
                    }
                    "Lịch booking" -> {
                        val filtered = if (selectedDateFilter == "Tất cả") bookings else bookings.filter { it.dateTime.contains(selectedDateFilter) }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { booking -> BookingCard(booking = booking, onComplete = {}, onCancel = {}, onDelete = { viewModel.itemToDelete.value = booking }) }
                        }
                    }
                    "Thống kê" -> {
                        AdminStatisticsScreen(viewModel)
                    }
                }
            }
        }
    }

    itemToDelete?.let { item ->
        ConfirmDeleteDialog(title = "Xóa", message = "Xóa mục này?", onDismiss = { viewModel.itemToDelete.value = null }) {
            viewModel.deleteItem(item)
        }
    }

    if (showAddUserDialog) {
        AddEditUserDialog(userToEdit, shops = shops, onDismiss = { viewModel.showAddUserDialog.value = false }) { n, e, p, pw, r, sId ->
            viewModel.saveUser(n, e, p, pw, r)
        }
    }

    if (showAddServiceDialog) {
        AddEditServiceDialog(serviceToEdit, onDismiss = { viewModel.showAddServiceDialog.value = false }) { n, d, p ->
            val priceInt = p.replace(".", "").replace(",", "").filter { it.isDigit() }.toIntOrNull() ?: 0
            viewModel.saveService(n, d, priceInt)
        }
    }

    if (showAddShopDialog) {
        AddEditShopDialog(shopToEdit, onDismiss = { viewModel.showAddShopDialog.value = false }) { n, a, p, pr, rDouble, i, listS, listB ->
            viewModel.saveShop(n, a, p, pr, rDouble, i)
        }
    }

}

@Composable
fun HeaderSection(onLogout: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Admin Dashboard", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Quản lý hệ thống", color = Color.Gray, fontSize = 14.sp)
        }
        IconButton(onClick = onLogout) { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White) }
    }
}

@Composable
fun TabButton(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(color = if (isSelected) Color(0xFFEBC14F) else Color(0xFF2C2C2C), shape = RoundedCornerShape(25.dp), modifier = Modifier.clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isSelected) Color.Black else Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = if (isSelected) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun SearchBarCustom(query: String, onQueryChange: (String) -> Unit) {
    TextField(value = query, onValueChange = onQueryChange, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Tìm kiếm...", color = Color.Gray) }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) }, singleLine = true)
}

@Composable
fun FilterChipCustom(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(modifier = Modifier.clickable(onClick = onClick), color = if (isSelected) Color(0xFFEBC14F) else Color(0xFF1E1E1E), shape = RoundedCornerShape(20.dp)) {
        Text(text = label, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = if (isSelected) Color.Black else Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ShopCard(shop: Shop, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).background(Color.DarkGray, RoundedCornerShape(12.dp)))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(shop.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(shop.address, color = Color.Gray, fontSize = 12.sp, maxLines = 1)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Gray) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFCF6679)) }
        }
    }
}

@Composable
fun UserCard(user: User, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color.DarkGray, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) { Text(user.name, color = Color.White, fontWeight = FontWeight.Bold); Text(user.email, color = Color.Gray, fontSize = 12.sp) }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Gray) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFCF6679)) }
        }
    }
}

@Composable
fun ServiceCard(service: Service, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) { Text(service.name, color = Color.White, fontWeight = FontWeight.Bold); Text("${service.duration} • ${service.price}", color = Color(0xFFEBC14F), fontSize = 13.sp) }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Gray) }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFCF6679)) }
        }
    }
}

@Composable
fun BookingCard(booking: Booking, onComplete: () -> Unit, onCancel: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(booking.service, color = Color.White, fontWeight = FontWeight.Bold)
            Text("${booking.service} - ${booking.barber}", color = Color.Gray, fontSize = 13.sp)
            Text(booking.dateTime, color = Color.Gray, fontSize = 13.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFCF6679)) } }
        }
    }
}

// Dialogs (ConfirmDeleteDialog, AddEditUserDialog, AddEditServiceDialog, AddEditShopDialog) are expected to exist in the project
