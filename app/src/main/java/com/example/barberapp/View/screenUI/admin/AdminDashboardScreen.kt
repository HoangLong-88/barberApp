package com.example.barberapp.View.screenUI.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.barberapp.View.component.*
import com.example.barberapp.ViewModel.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalLocale

@Composable
fun AdminDashboardScreen(
    authVM: AuthVM,
    userVM: UserVM,
    shopVM: ShopVM,
    navController: NavController
) {
    val adminVM: AdminViewModel = viewModel()
    val currentTab by adminVM.currentTab
    val searchQuery by adminVM.searchQuery

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            AdminHeaderSection(authVM, userVM, shopVM, navController)
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
                    AdminTabButton(label, icon, currentTab == value) { adminVM.setCurrentTab(value) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar (Smart Search)
            if (currentTab != "Hồ sơ" && currentTab != "Thống kê") {
                SearchBarCustom(
                    query = searchQuery,
                    onQueryChange = { adminVM.setSearchQuery(it) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Sub-Header Content (Filters)
            if (currentTab != "Hồ sơ" && currentTab != "Thống kê") {
                TabHeaderTitle(currentTab, adminVM)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Main Content Area
            Box(modifier = Modifier.weight(1f)) {
                when (currentTab) {
                    "Tiệm" -> ShopTabContent(adminVM)
                    "Tài khoản" -> UserTabContent(adminVM)
                    "Dịch vụ" -> ServiceTabContent(adminVM)
                    "Lịch booking" -> BookingTabContent(adminVM)
                    "Thống kê" -> AdminStatisticsScreen(adminVM)
                    "Hồ sơ" -> AdminProfileScreen(authVM, userVM, shopVM)
                }
            }
        }
    }
    ViewDialogs(adminVM)
}

@Composable
fun TabHeaderTitle(currentTab: String, viewModel: AdminViewModel) {
    val selectedUserFilter by viewModel.selectedUserFilter
    val selectedBookingStatus by viewModel.selectedBookingStatus
    val selectedDateFilter by viewModel.selectedDateFilter

    when (currentTab) {
        "Tiệm" -> {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Danh sách tiệm (${viewModel.shops.size})", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { viewModel.shopToEdit.value = null; viewModel.showAddShopDialog.value = true }, modifier = Modifier.size(36.dp).background(Color(0xFFEBC14F), RoundedCornerShape(8.dp))) { Icon(Icons.Default.Add, null, tint = Color.Black) }
            }
        }
        "Tài khoản" -> {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Tất cả", "KH", "NV", "QL").forEach { label ->
                        AdminFilterChipCustom(label, selectedUserFilter == label) { viewModel.setSelectedUserFilter(label) }
                    }
                }
                IconButton(onClick = { viewModel.userToEdit.value = null; viewModel.showAddUserDialog.value = true }, modifier = Modifier.size(36.dp).background(Color(0xFFEBC14F), RoundedCornerShape(8.dp))) { Icon(Icons.Default.Add, null, tint = Color.Black) }
            }
        }
        "Dịch vụ" -> {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                val selectedShopForService by viewModel.selectedShopForService
                Text("Dịch vụ: ${selectedShopForService?.name ?: "..."}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { viewModel.serviceToEdit.value = null; viewModel.showAddServiceDialog.value = true }, modifier = Modifier.size(36.dp).background(Color(0xFFEBC14F), RoundedCornerShape(8.dp))) { Icon(Icons.Default.Add, null, tint = Color.Black) }
            }
        }
        "Lịch booking" -> {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // 1. Lọc trạng thái (Status Filter)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FilterList, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Trạng thái:", color = Color.Gray, fontSize = 12.sp)
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Tất cả", "Pending", "Completed", "Cancelled")) { status ->
                        AdminFilterChipCustom(status, selectedBookingStatus == status) { viewModel.setSelectedBookingStatus(status) }
                    }
                }
                // 4. Phân loại theo thời gian (Time Filter)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thời gian:", color = Color.Gray, fontSize = 12.sp)
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("Hôm nay", "Sắp tới", "Tất cả")) { time ->
                        AdminFilterChipCustom(time, selectedDateFilter == time) { viewModel.setSelectedDateFilter(time) }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingTabContent(viewModel: AdminViewModel) {
    val selectedBookingStatus by viewModel.selectedBookingStatus
    val selectedDateFilter by viewModel.selectedDateFilter
    val searchQuery by viewModel.searchQuery
    
    val sdf = SimpleDateFormat("dd/MM/yyyy", LocalLocale.current.platformLocale)
    val today = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.time
    val todayStr = sdf.format(today)
    
    val filtered = viewModel.bookings.filter { booking ->
        // 2. Tìm kiếm thông minh (Tên, SĐT, Tên thợ, hoặc Mã ID)
        val matchesSearch = booking.customerName.contains(searchQuery, true) || 
                          booking.customerPhone.contains(searchQuery, true) ||
                          booking.barberName.contains(searchQuery, true) ||
                          booking.id.contains(searchQuery, true)
        
        // 1. Lọc trạng thái
        val matchesStatus = if (selectedBookingStatus == "Tất cả") true 
                           else booking.status.name.equals(selectedBookingStatus, true)
        
        // 4. Lọc thời gian (Hôm nay, Sắp tới, Tất cả)
        val matchesDate = when (selectedDateFilter) {
            "Hôm nay" -> booking.bookingDate == todayStr
            "Sắp tới" -> {
                try {
                    val bDate = sdf.parse(booking.bookingDate)
                    bDate != null && bDate.after(today)
                } catch (e: Exception) { false }
            }
            else -> true
        }
        
        matchesSearch && matchesStatus && matchesDate
    }

    if (filtered.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.EventBusy, null, tint = Color.DarkGray, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Không có lịch hẹn phù hợp", color = Color.Gray, fontSize = 16.sp)
                if (searchQuery.isNotEmpty()) {
                    Text("Thử tìm kiếm với từ khóa khác", color = Color.DarkGray, fontSize = 12.sp)
                }
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp), 
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Text(
                    "Kết quả: ${filtered.size} lịch hẹn", 
                    color = Color(0xFFEBC14F), 
                    fontSize = 12.sp, 
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            items(filtered, key = { it.id }) { booking ->
                // 3. Giao diện Card chuyên nghiệp
                BookingCardForAdmin(
                    booking = booking,
                    onComplete = { viewModel.confirmBooking(booking.id) },
                    onCancel = { viewModel.cancelBooking(booking.id) },
                    onDelete = { viewModel.deleteBooking(booking.id) }
                )
            }
        }
    }
}

// Giữ nguyên các Content Tab khác của đồng nghiệp
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
    val searchQuery by viewModel.searchQuery
    val selectedShopFilterForEmployee by viewModel.selectedShopFilterForEmployee
    var filtered = viewModel.users.filter { it.name.contains(searchQuery, true) || it.email.contains(searchQuery, true) || it.phone.contains(searchQuery, true) }
    if (selectedUserFilter != "Tất cả") {
        filtered = filtered.filter {
            when (selectedUserFilter) {
                "KH" -> it.role == "customer"
                "NV" -> it.role == "employee"
                "QL" -> it.role == "manager"
                else -> true
            }
        }
    }
    if (selectedUserFilter == "NV" && selectedShopFilterForEmployee != "Tất cả") {
        filtered = filtered.filter { it.shopId == selectedShopFilterForEmployee }
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (selectedUserFilter == "NV") item { EmployeeShopFilter(viewModel) }
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
}
