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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barberapp.ViewModel.auth.AuthVM
import com.example.barberapp.ViewModel.customer.UserVM
import com.example.barberapp.Model.entities.ServiceItem
import com.example.barberapp.Model.entities.ShopItem
import com.example.barberapp.Model.entities.UserItem
import com.example.barberapp.ViewModel.admin.AdminViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel = viewModel(),
    authVM: AuthVM,
    userVM: UserVM,
    navController: NavController
    ) {
    val currentTab by viewModel.currentTab
    val selectedUserFilter by viewModel.selectedUserFilter
    val selectedDateFilter by viewModel.selectedDateFilter
    val selectedShopForService by viewModel.selectedShopForService
    val searchQuery by viewModel.searchQuery

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            HeaderSection(authVM,userVM,navController)
            Spacer(modifier = Modifier.height(20.dp))

            // Main Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    TabButton(
                        "Tiệm",
                        Icons.Default.Home,
                        currentTab == "Tiệm"
                    ) { viewModel.setCurrentTab("Tiệm") }
                }
                item {
                    TabButton(
                        "Tài khoản",
                        Icons.Default.Person,
                        currentTab == "Tài khoản"
                    ) { viewModel.setCurrentTab("Tài khoản") }
                }
                item {
                    TabButton(
                        "Dịch vụ",
                        Icons.Default.Build,
                        currentTab == "Dịch vụ"
                    ) { viewModel.setCurrentTab("Dịch vụ") }
                }
                item {
                    TabButton(
                        "Lịch booking",
                        Icons.Default.DateRange,
                        currentTab == "Lịch booking"
                    ) { viewModel.setCurrentTab("Lịch booking") }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar (Only for Shops and Services)
            if (currentTab == "Tiệm" || currentTab == "Dịch vụ") {
                SearchBarCustom(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sub-Header Content Based on Tab
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (currentTab) {
                        "Tiệm" -> Text(
                            "Danh sách tiệm (${viewModel.shops.size})",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        "Tài khoản" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Tất cả", "KH", "NV", "QL").forEach { label ->
                                    FilterChipCustom(
                                        label,
                                        selectedUserFilter == label
                                    ) { viewModel.setSelectedUserFilter(label) }
                                }
                            }
                        }

                        "Dịch vụ" -> Text(
                            "Dịch vụ tại: ${selectedShopForService?.name ?: "..."}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        "Lịch booking" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(
                                    "Tất cả",
                                    "Mon",
                                    "Tue",
                                    "Wed",
                                    "Thu",
                                    "Fri",
                                    "Sat"
                                ).forEach { day ->
                                    FilterChipCustom(
                                        day,
                                        selectedDateFilter == day
                                    ) { viewModel.setSelectedDateFilter(day) }
                                }
                            }
                        }
                    }

                    if (currentTab != "Lịch booking") {
                        IconButton(
                            onClick = {
                                when (currentTab) {
                                    "Tiệm" -> {
                                        viewModel.shopToEdit.value =
                                            null; viewModel.showAddShopDialog.value = true
                                    }

                                    "Tài khoản" -> {
                                        viewModel.userToEdit.value =
                                            null; viewModel.showAddUserDialog.value = true
                                    }

                                    "Dịch vụ" -> {
                                        viewModel.serviceToEdit.value =
                                            null; viewModel.showAddServiceDialog.value = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFEBC14F), RoundedCornerShape(8.dp))
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
                            it.name.contains(searchQuery, ignoreCase = true) || it.address.contains(
                                searchQuery,
                                ignoreCase = true
                            )
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filteredShops, key = { it.id }) { shop ->
                                ShopCard(
                                    shop,
                                    onEdit = {
                                        viewModel.shopToEdit.value =
                                            shop; viewModel.showAddShopDialog.value = true
                                    },
                                    onDelete = { viewModel.itemToDelete.value = shop })
                            }
                        }
                    }

                    "Tài khoản" -> {
                        val filtered = if (selectedUserFilter == "Tất cả") viewModel.users
                        else viewModel.users.filter {
                            when (selectedUserFilter) {
                                "KH" -> it.role == "customer"
                                "NV" -> it.role == "employee"
                                "QL" -> it.role == "manager"
                                else -> true
                            }
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { user ->
                                UserCard(
                                    user,
                                    onEdit = {
                                        viewModel.userToEdit.value =
                                            user; viewModel.showAddUserDialog.value = true
                                    },
                                    onDelete = { viewModel.itemToDelete.value = user })
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
                                ServiceCard(
                                    service,
                                    onEdit = {
                                        viewModel.serviceToEdit.value =
                                            service; viewModel.showAddServiceDialog.value = true
                                    },
                                    onDelete = { viewModel.itemToDelete.value = service })
                            }
                        }
                    }

                    "Lịch booking" -> {
                        val filtered = if (selectedDateFilter == "Tất cả") viewModel.bookings
                        else viewModel.bookings.filter { it.dateTime.contains(selectedDateFilter) }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { booking ->
                                BookingCard(
                                    booking = booking,
                                    onComplete = { /* Handle complete */ },
                                    onCancel = { /* Handle cancel */ },
                                    onDelete = { viewModel.itemToDelete.value = booking }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    ViewDialogs(viewModel)
}

@Composable
fun ViewDialogs(viewModel: AdminViewModel) {
    val itemToDelete by viewModel.itemToDelete
    val showAddUserDialog by viewModel.showAddUserDialog
    val showAddServiceDialog by viewModel.showAddServiceDialog
    val showAddShopDialog by viewModel.showAddShopDialog

    itemToDelete?.let { item ->
        ConfirmDeleteDialog(
            title = when (item) {
                is UserItem -> "Xóa tài khoản?"
                is ShopItem -> "Xóa tiệm?"
                is ServiceItem -> "Xóa dịch vụ?"
                else -> "Xác nhận xóa?"
            },
            message = "Hành động này không thể hoàn tác.",
            onDismiss = { viewModel.itemToDelete.value = null },
            onConfirm = { viewModel.deleteItem(item) }
        )
    }

    if (showAddUserDialog) {
        AddEditUserDialog(
            viewModel.userToEdit.value,
            onDismiss = { viewModel.showAddUserDialog.value = false }) { n, e, p, r ->
            viewModel.saveUser(n, e, p, r)
        }
    }

    if (showAddServiceDialog) {
        AddEditServiceDialog(
            viewModel.serviceToEdit.value,
            onDismiss = { viewModel.showAddServiceDialog.value = false }) { n, d, p ->
            viewModel.saveService(n, d, p)
        }
    }

    if (showAddShopDialog) {
        AddEditShopDialog(
            viewModel.shopToEdit.value,
            onDismiss = { viewModel.showAddShopDialog.value = false }) { n, a, p, pr, r, i ->
            viewModel.saveShop(n, a, p, pr, r, i)
        }
    }
}

@Composable
fun HeaderSection(authVM: AuthVM,userVM: UserVM,navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Admin Dashboard",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Quản lý hệ thống", color = Color.Gray, fontSize = 14.sp)
        }
        IconButton(onClick = { authVM.logOut(navController,userVM) }) {
            Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun TabButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) Color(0xFFEBC14F) else Color(0xFF2C2C2C),
        shape = RoundedCornerShape(25.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                null,
                tint = if (isSelected) Color.Black else Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                label,
                color = if (isSelected) Color.Black else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
