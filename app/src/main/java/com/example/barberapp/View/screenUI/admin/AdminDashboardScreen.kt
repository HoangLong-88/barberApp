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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
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
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.example.barberapp.View.component.AdminFilterChipCustom
import com.example.barberapp.View.component.AdminHeaderSection
import com.example.barberapp.View.component.AdminTabButton
import com.example.barberapp.View.component.BookingCard
import com.example.barberapp.View.component.SearchBarCustom
import com.example.barberapp.View.component.ServiceDetailsCardInCustomer
import com.example.barberapp.View.component.ShopCard
import com.example.barberapp.View.component.UserCard
import com.example.barberapp.ViewModel.AdminViewModel
import com.example.barberapp.ViewModel.AuthVM
import com.example.barberapp.ViewModel.ShopVM
import com.example.barberapp.ViewModel.UserVM

@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel = viewModel(),
    authVM: AuthVM,
    userVM: UserVM,
    shopVM: ShopVM,
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
            AdminHeaderSection(authVM, userVM,shopVM, navController)
            Spacer(modifier = Modifier.height(20.dp))

            // Main Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val tabs = listOf(
                    Triple("Tiệm", Icons.Default.Home, "Tiệm"),
                    Triple("Tài khoản", Icons.Default.Person, "Tài khoản"),
                    Triple("Dịch vụ", Icons.Default.Build, "Dịch vụ"),
                    Triple("Lịch booking", Icons.Default.DateRange, "Lịch booking"),
                    Triple("Thống kê", Icons.Default.BarChart, "Thống kê"),
                    Triple("Hồ sơ", Icons.Default.AccountCircle, "Hồ sơ")
                )

                items(tabs) { (label, icon, value) ->
                    AdminTabButton(
                        label,
                        icon,
                        currentTab == value
                    ) { viewModel.setCurrentTab(value) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar & Sub-header logic
            if (currentTab == "Tiệm" || currentTab == "Dịch vụ") {
                SearchBarCustom(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sub-Header Content Based on Tab
            if (currentTab != "Hồ sơ" && currentTab != "Thống kê") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabHeaderTitle(currentTab, viewModel)

                    if (currentTab != "Lịch booking") {
                        IconButton(
                            onClick = { openAddDialog(currentTab, viewModel) },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFEBC14F), RoundedCornerShape(8.dp))
                        ) { Icon(Icons.Default.Add, null, tint = Color.Black) }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Main Content Area
            Box(modifier = Modifier.weight(1f)) {
                when (currentTab) {
                    "Tiệm" -> ShopTabContent(viewModel)
                    "Tài khoản" -> UserTabContent(viewModel)
                    "Dịch vụ" -> ServiceTabContent(viewModel)
                    "Lịch booking" -> BookingTabContent(viewModel)
                    "Thống kê" -> AdminStatisticsScreen(viewModel)
                    "Hồ sơ" -> AdminProfileScreen(authVM, userVM, navController)
                }
            }
        }
    }

    ViewDialogs(viewModel)
}

@Composable
fun TabHeaderTitle(currentTab: String, viewModel: AdminViewModel) {
    val selectedUserFilter by viewModel.selectedUserFilter
    val selectedDateFilter by viewModel.selectedDateFilter
    val selectedShopForService by viewModel.selectedShopForService

    when (currentTab) {
        "Tiệm" -> Text("Danh sách tiệm (${viewModel.shops.size})", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        "Tài khoản" -> {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Tất cả", "KH", "NV", "QL").forEach { label ->
                    AdminFilterChipCustom(label, selectedUserFilter == label) { viewModel.setSelectedUserFilter(label) }
                }
            }
        }
        "Dịch vụ" -> Text("Dịch vụ: ${selectedShopForService?.name ?: "..."}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        "Lịch booking" -> {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("Tất cả", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")) { day ->
                    AdminFilterChipCustom(day, selectedDateFilter == day) { viewModel.setSelectedDateFilter(day) }
                }
            }
        }
    }
}

private fun openAddDialog(currentTab: String, viewModel: AdminViewModel) {
    when (currentTab) {
        "Tiệm" -> { viewModel.shopToEdit.value = null; viewModel.showAddShopDialog.value = true }
        "Tài khoản" -> { viewModel.userToEdit.value = null; viewModel.showAddUserDialog.value = true }
        "Dịch vụ" -> { viewModel.serviceToEdit.value = null; viewModel.showAddServiceDialog.value = true }
    }
}

// Các hàm bổ trợ để code sạch hơn
@Composable
fun ShopTabContent(viewModel: AdminViewModel) {
    val searchQuery by viewModel.searchQuery
    val filtered = viewModel.shops.filter { it.name.contains(searchQuery, true) || it.address.contains(searchQuery, true) }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(filtered, key = { it.id }) { shop ->
            ShopCard(shop, onEdit = { viewModel.shopToEdit.value = shop; viewModel.showAddShopDialog.value = true },
                onDelete = { viewModel.itemToDelete.value = shop })
        }
    }
}

@Composable
fun UserTabContent(viewModel: AdminViewModel) {
    val selectedUserFilter by viewModel.selectedUserFilter
    val selectedShopFilterForEmployee by viewModel.selectedShopFilterForEmployee

    var filtered = if (selectedUserFilter == "Tất cả") viewModel.users
    else viewModel.users.filter {
        when (selectedUserFilter) {
            "KH" -> it.role == "customer"
            "NV" -> it.role == "employee"
            "QL" -> it.role == "manager"
            else -> true
        }
    }
    if (selectedUserFilter == "NV" && selectedShopFilterForEmployee != "Tất cả") {
        filtered = filtered.filter { it.shopId == selectedShopFilterForEmployee }
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (selectedUserFilter == "NV") {
            item { EmployeeShopFilter(viewModel) }
        }
        items(filtered, key = { it.id }) { user ->
            UserCard(user, onEdit = { viewModel.userToEdit.value = user; viewModel.showAddUserDialog.value = true },
                onDelete = { viewModel.itemToDelete.value = user })
        }
    }
}

@Composable
fun EmployeeShopFilter(viewModel: AdminViewModel) {
    val selectedShopFilterForEmployee by viewModel.selectedShopFilterForEmployee
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text("Lọc theo nơi làm việc:", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { AdminFilterChipCustom("Tất cả tiệm", selectedShopFilterForEmployee == "Tất cả") { viewModel.updateShopFilterForEmployee("Tất cả") } }
            items(viewModel.shops) { shop ->
                AdminFilterChipCustom(shop.name, selectedShopFilterForEmployee == shop.id) { viewModel.updateShopFilterForEmployee(shop.id) }
            }
        }
    }
}

@Composable
fun ServiceTabContent(viewModel: AdminViewModel) {
    val searchQuery by viewModel.searchQuery
    val selectedShopForService by viewModel.selectedShopForService
    val shopServices = viewModel.services.filter { it.shopId == selectedShopForService?.id && it.name.contains(searchQuery, true) }

    Column {
        Text("Chọn tiệm:", color = Color.Gray, fontSize = 12.sp)
        LazyRow(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.shops) { shop ->
                AdminFilterChipCustom(shop.name, selectedShopForService?.id == shop.id) { viewModel.setSelectedShopForService(shop) }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(shopServices, key = { it.id }) { service ->
                ServiceDetailsCardInCustomer(service, onEdit = { viewModel.serviceToEdit.value = service; viewModel.showAddServiceDialog.value = true },
                    onDelete = { viewModel.itemToDelete.value = service })
            }
        }
    }
}

@Composable
fun BookingTabContent(viewModel: AdminViewModel) {
    val selectedDateFilter by viewModel.selectedDateFilter
    val filtered = if (selectedDateFilter == "Tất cả") viewModel.bookings
    else viewModel.bookings.filter { it.bookingDate.contains(selectedDateFilter) }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(filtered, key = { it.id }) { booking ->
            // Sử dụng model Booking mới nhất
            BookingCard(booking = booking, onComplete = {}, onCancel = {}, onDelete = { viewModel.itemToDelete.value = booking })
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
            title = when (item) { is User -> "Xóa tài khoản?"; is Shop -> "Xóa tiệm?"; is Service -> "Xóa dịch vụ?"; else -> "Xác nhận?" },
            message = "Hành động này không thể hoàn tác.",
            onDismiss = { viewModel.itemToDelete.value = null },
            onConfirm = { viewModel.deleteItem(item) }
        )
    }

    if (showAddUserDialog) AddEditUserDialog(viewModel.userToEdit.value, viewModel.shops, { viewModel.showAddUserDialog.value = false }) { n, e, p, pw, r, sId -> viewModel.saveUser(n, e, p, pw, r, sId) }
    if (showAddServiceDialog) AddEditServiceDialog(viewModel.serviceToEdit.value, { viewModel.showAddServiceDialog.value = false }) { n, d, p -> viewModel.saveService(n, d, p) }
    if (showAddShopDialog) AddEditShopDialog(viewModel.shopToEdit.value, { viewModel.showAddShopDialog.value = false }) { n, a, p, pr, r, i, listS, listB ->
        viewModel.saveShop(viewModel.shopToEdit.value?.id, n, a, p, pr, r, i, listS, listB)
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

