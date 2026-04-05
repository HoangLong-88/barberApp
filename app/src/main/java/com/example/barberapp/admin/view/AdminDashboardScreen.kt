package com.example.barberapp.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.admin.controller.AdminController
import com.example.barberapp.admin.model.BookingItem
import com.example.barberapp.admin.model.ServiceItem
import com.example.barberapp.admin.model.ShopItem
import com.example.barberapp.admin.model.UserItem

@Composable
fun AdminDashboardScreen() {
    val controller = remember { AdminController() }
    val currentTab by controller.currentTab
    val selectedUserFilter by controller.selectedUserFilter
    val selectedDateFilter by controller.selectedDateFilter
    val selectedShopForService by controller.selectedShopForService
    val searchQuery by controller.searchQuery
    
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            HeaderSection()
            Spacer(modifier = Modifier.height(20.dp))

            // Main Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item { TabButton("Tiệm", Icons.Default.Home, currentTab == "Tiệm") { controller.currentTab.value = "Tiệm" } }
                item { TabButton("Tài khoản", Icons.Default.Person, currentTab == "Tài khoản") { controller.currentTab.value = "Tài khoản" } }
                item { TabButton("Dịch vụ", Icons.Default.Build, currentTab == "Dịch vụ") { controller.currentTab.value = "Dịch vụ" } }
                item { TabButton("Lịch booking", Icons.Default.DateRange, currentTab == "Lịch booking") { controller.currentTab.value = "Lịch booking" } }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar (Only for Shops and Services)
            if (currentTab == "Tiệm" || currentTab == "Dịch vụ") {
                SearchBarCustom(
                    query = searchQuery,
                    onQueryChange = { controller.searchQuery.value = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sub-Header Content Based on Tab
            Column {
                if (currentTab == "Dịch vụ") {
                    Text("Chọn tiệm để quản lý dịch vụ:", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(controller.shops) { shop ->
                            FilterChipCustom(
                                label = shop.name,
                                isSelected = selectedShopForService?.id == shop.id,
                                onClick = { controller.selectedShopForService.value = shop }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    when (currentTab) {
                        "Tiệm" -> Text("Danh sách tiệm (${controller.shops.size})", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        "Tài khoản" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Tất cả", "KH", "NV", "QL").forEach { label ->
                                    FilterChipCustom(label, selectedUserFilter == label) { controller.selectedUserFilter.value = label }
                                }
                            }
                        }
                        "Dịch vụ" -> Text("Dịch vụ tại: ${selectedShopForService?.name ?: "..."}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        "Lịch booking" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Tất cả", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                                    FilterChipCustom(day, selectedDateFilter == day) { controller.selectedDateFilter.value = day }
                                }
                            }
                        }
                    }

                    if (currentTab != "Lịch booking") {
                        IconButton(
                            onClick = {
                                when (currentTab) {
                                    "Tiệm" -> { controller.shopToEdit.value = null; controller.showAddShopDialog.value = true }
                                    "Tài khoản" -> { controller.userToEdit.value = null; controller.showAddUserDialog.value = true }
                                    "Dịch vụ" -> { controller.serviceToEdit.value = null; controller.showAddServiceDialog.value = true }
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
                        val filteredShops = controller.shops.filter { 
                            it.name.contains(searchQuery, ignoreCase = true) || it.address.contains(searchQuery, ignoreCase = true)
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filteredShops, key = { it.id }) { shop ->
                                ShopCard(shop, onEdit = { controller.shopToEdit.value = shop; controller.showAddShopDialog.value = true }, onDelete = { controller.itemToDelete.value = shop })
                            }
                        }
                    }
                    "Tài khoản" -> {
                        val filtered = if (selectedUserFilter == "Tất cả") controller.users 
                                      else controller.users.filter { 
                                          when(selectedUserFilter) { 
                                              "KH" -> it.role == "customer"
                                              "NV" -> it.role == "employee"
                                              "QL" -> it.role == "manager"
                                              else -> true 
                                          }
                                      }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { user ->
                                UserCard(user, onEdit = { controller.userToEdit.value = user; controller.showAddUserDialog.value = true }, onDelete = { controller.itemToDelete.value = user })
                            }
                        }
                    }
                    "Dịch vụ" -> {
                        // Lọc dịch vụ theo Tiệm và Tìm kiếm
                        val shopServices = controller.services.filter { 
                            it.shopId == selectedShopForService?.id && 
                            it.name.contains(searchQuery, ignoreCase = true)
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(shopServices, key = { it.id }) { service ->
                                ServiceCard(service, onEdit = { controller.serviceToEdit.value = service; controller.showAddServiceDialog.value = true }, onDelete = { controller.itemToDelete.value = service })
                            }
                        }
                    }
                    "Lịch booking" -> {
                        val filtered = if (selectedDateFilter == "Tất cả") controller.bookings 
                                      else controller.bookings.filter { it.dateTime.contains(selectedDateFilter) }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { booking ->
                                BookingCard(
                                    booking = booking,
                                    onComplete = { /* Handle complete */ },
                                    onCancel = { /* Handle cancel */ },
                                    onDelete = { controller.itemToDelete.value = booking }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    ViewDialogs(controller)
}

@Composable
fun ViewDialogs(controller: AdminController) {
    val itemToDelete by controller.itemToDelete
    val showAddUserDialog by controller.showAddUserDialog
    val showAddServiceDialog by controller.showAddServiceDialog
    val showAddShopDialog by controller.showAddShopDialog

    itemToDelete?.let { item ->
        ConfirmDeleteDialog(
            title = when(item) {
                is UserItem -> "Xóa tài khoản?"
                is ShopItem -> "Xóa tiệm?"
                is ServiceItem -> "Xóa dịch vụ?"
                else -> "Xác nhận xóa?"
            },
            message = "Hành động này không thể hoàn tác.",
            onDismiss = { controller.itemToDelete.value = null },
            onConfirm = { controller.deleteItem(item) }
        )
    }

    if (showAddUserDialog) {
        AddEditUserDialog(controller.userToEdit.value, onDismiss = { controller.showAddUserDialog.value = false }) { n, e, p, r ->
            controller.saveUser(n, e, p, r)
        }
    }

    if (showAddServiceDialog) {
        AddEditServiceDialog(controller.serviceToEdit.value, onDismiss = { controller.showAddServiceDialog.value = false }) { n, d, p ->
            controller.saveService(n, d, p)
        }
    }

    if (showAddShopDialog) {
        AddEditShopDialog(controller.shopToEdit.value, onDismiss = { controller.showAddShopDialog.value = false }) { n, a, p, pr, r, i ->
            controller.saveShop(n, a, p, pr, r, i)
        }
    }
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
fun TabButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
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
