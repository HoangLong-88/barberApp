package com.example.barberapp.View.screenUI.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barberapp.Model.entities.ServiceItem
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.example.barberapp.View.component.AdminFilterChipCustom
import com.example.barberapp.View.component.AdminHeaderSection
import com.example.barberapp.View.component.AdminTabButton
import com.example.barberapp.View.component.BookingCard
import com.example.barberapp.View.component.SearchBarCustom
import com.example.barberapp.View.component.ServiceCard
import com.example.barberapp.View.component.ShopCard
import com.example.barberapp.View.component.UserCard
import com.example.barberapp.ViewModel.AdminViewModel
import com.example.barberapp.ViewModel.AuthVM
import com.example.barberapp.ViewModel.UserVM

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
    val selectedShopFilterForEmployee by viewModel.selectedShopFilterForEmployee

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AdminHeaderSection(authVM, userVM, navController)
            Spacer(modifier = Modifier.height(20.dp))

            // Main Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    AdminTabButton(
                        "Tiệm",
                        Icons.Default.Home,
                        currentTab == "Tiệm"
                    ) { viewModel.setCurrentTab("Tiệm") }
                }
                item {
                    AdminTabButton(
                        "Tài khoản",
                        Icons.Default.Person,
                        currentTab == "Tài khoản"
                    ) { viewModel.setCurrentTab("Tài khoản") }
                }
                item {
                    AdminTabButton(
                        "Dịch vụ",
                        Icons.Default.Build,
                        currentTab == "Dịch vụ"
                    ) { viewModel.setCurrentTab("Dịch vụ") }
                }
                item {
                    AdminTabButton(
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
                            AdminFilterChipCustom(
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
                            Column {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Tất cả", "KH", "NV", "QL").forEach { label ->
                                        AdminFilterChipCustom(
                                            label,
                                            selectedUserFilter == label
                                        ) { viewModel.setSelectedUserFilter(label) }
                                    }
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
                                    AdminFilterChipCustom(
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
                                        viewModel.shopToEdit.value = shop
                                        viewModel.showAddShopDialog.value = true
                                    },
                                    onDelete = { viewModel.itemToDelete.value = shop })
                            }
                        }
                    }

                    "Tài khoản" -> {
                        // 1. Lọc theo chức vụ (KH, NV, QL) trước
                        var filtered = if (selectedUserFilter == "Tất cả") viewModel.users
                        else viewModel.users.filter {
                            when (selectedUserFilter) {
                                "KH" -> it.role == "customer"
                                "NV" -> it.role == "employee"
                                "QL" -> it.role == "manager"
                                else -> true
                            }
                        }

                        // 2. Lọc phụ: Nếu đang chọn "NV" và người dùng chọn một Tiệm cụ thể, lọc tiếp theo shopId
                        if (selectedUserFilter == "NV" && selectedShopFilterForEmployee != "Tất cả") {
                            filtered = filtered.filter { it.shopId == selectedShopFilterForEmployee }
                        }

                        // 3. Chỉ sử dụng DUY NHẤT một LazyColumn ở đây
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                            // --- HÀNG CHIPS PHỤ: Xuất hiện ở ĐẦU danh sách khi đang chọn lọc "NV" ---
                            if (selectedUserFilter == "NV") {
                                item {
                                    Column {
                                        Text(
                                            text = "Lọc theo nơi làm việc:",
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            item {
                                                AdminFilterChipCustom(
                                                    label = "Tất cả tiệm",
                                                    isSelected = selectedShopFilterForEmployee == "Tất cả",
                                                    onClick = { viewModel.updateShopFilterForEmployee("Tất cả") }
                                                )
                                            }
                                            items(viewModel.shops) { shop ->
                                                AdminFilterChipCustom(
                                                    label = shop.name,
                                                    isSelected = selectedShopFilterForEmployee == shop.id,
                                                    onClick = { viewModel.updateShopFilterForEmployee(shop.id) }
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }

                            // --- HIỂN THỊ DANH SÁCH TÀI KHOẢN SAU KHI ĐÃ LỌC ---
                            if (filtered.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "Không có tài khoản nào phù hợp",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            } else {
                                items(filtered, key = { it.id }) { user ->
                                    Column {
                                        UserCard(
                                            user = user,
                                            onEdit = {
                                                viewModel.userToEdit.value = user
                                                viewModel.showAddUserDialog.value = true
                                            },
                                            onDelete = {
                                                viewModel.itemToDelete.value = user
                                            }
                                        )
                                    }
                                }
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
                                        viewModel.serviceToEdit.value = service;
                                        viewModel.showAddServiceDialog.value = true
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
                is User -> "Xóa tài khoản?"
                is Shop -> "Xóa tiệm?"
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
            shops = viewModel.shops,
            onDismiss = { viewModel.showAddUserDialog.value = false }) { n, e, p, pw, r, sId ->
            viewModel.saveUser(n, e, p, pw, r, sId)
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
            onDismiss = {
                viewModel.showAddShopDialog.value = false
            }) { n, a, p, pr, r, i, listS, listB ->
            viewModel.saveShop(
                viewModel.shopToEdit.value?.id, n, a, p,
                pr, r, i, listS, listB
            )
        }
    }
}

