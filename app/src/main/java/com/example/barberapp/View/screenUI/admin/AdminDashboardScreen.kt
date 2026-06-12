package com.example.barberapp.View.screenUI.admin

import androidx.compose.foundation.background
<<<<<<< HEAD
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
=======
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
>>>>>>> feature/admin
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
<<<<<<< HEAD
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
=======
import com.example.barberapp.Model.entities.BookingItem
import com.example.barberapp.Model.entities.ServiceItem
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.UserItem
import androidx.navigation.NavController
import com.example.barberapp.ViewModel.AuthVM
import com.example.barberapp.ViewModel.UserVM
// Local self-contained screen — no ViewModel dependency here
>>>>>>> feature/admin

@Composable
fun AdminDashboardScreen(
    navController: NavController,
    authVM: AuthVM,
<<<<<<< HEAD
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
=======
    userVM: UserVM
) {
    // Use local UI state to keep this screen self-contained and avoid compile errors
    var currentTab by remember { mutableStateOf("Tiệm") }
    var selectedUserFilter by remember { mutableStateOf("Tất cả") }
    var selectedDateFilter by remember { mutableStateOf("Tất cả") }
    var selectedShopForService by remember { mutableStateOf<Shop?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // sample lists (empty by default, can be populated later)
    val shops = remember { mutableStateListOf<Shop>() }
    val users = remember { mutableStateListOf<UserItem>() }
    val services = remember { mutableStateListOf<ServiceItem>() }
    val bookings = remember { mutableStateListOf<BookingItem>() }

    // dialog & selection states
    var showAddUserDialog by remember { mutableStateOf(false) }
    var showAddServiceDialog by remember { mutableStateOf(false) }
    var showAddShopDialog by remember { mutableStateOf(false) }
    var userToEdit by remember { mutableStateOf<UserItem?>(null) }
    var serviceToEdit by remember { mutableStateOf<ServiceItem?>(null) }
    var shopToEdit by remember { mutableStateOf<Shop?>(null) }
    var itemToDelete by remember { mutableStateOf<Any?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            HeaderSection()
>>>>>>> feature/admin
            Spacer(modifier = Modifier.height(20.dp))

            // Main Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
<<<<<<< HEAD
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
=======
                item { TabButton("Thống kê", Icons.Default.Assessment, currentTab == "Thống kê") { currentTab = "Thống kê" } }
                item { TabButton("Tiệm", Icons.Default.Home, currentTab == "Tiệm") { currentTab = "Tiệm" } }
                item { TabButton("Tài khoản", Icons.Default.Person, currentTab == "Tài khoản") { currentTab = "Tài khoản" } }
                item { TabButton("Dịch vụ", Icons.Default.Build, currentTab == "Dịch vụ") { currentTab = "Dịch vụ" } }
                item { TabButton("Lịch booking", Icons.Default.DateRange, currentTab == "Lịch booking") { currentTab = "Lịch booking" } }
>>>>>>> feature/admin
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar (for Shops and Services)
            if (currentTab == "Tiệm" || currentTab == "Dịch vụ") {
                SearchBarCustom(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sub-Header
            Column {
                if (currentTab == "Dịch vụ") {
                    Text("Chọn tiệm để quản lý dịch vụ:", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(shops) { shop ->
                            FilterChipCustom(
                                label = shop.name,
                                isSelected = selectedShopForService?.id == shop.id,
                                onClick = { selectedShopForService = shop }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

<<<<<<< HEAD
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
=======
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        when (currentTab) {
                            "Tiệm" -> Text("Danh sách tiệm (${shops.size})", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            "Tài khoản" -> {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Tất cả", "KH", "NV", "QL").forEach { label ->
                                        FilterChipCustom(label, selectedUserFilter == label) { selectedUserFilter = label }
>>>>>>> feature/admin
                                    }
                                }
                            }
                            "Dịch vụ" -> Text("Dịch vụ tại: ${selectedShopForService?.name ?: "..."}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            "Lịch booking" -> {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Tất cả", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                                        FilterChipCustom(day, selectedDateFilter == day) { selectedDateFilter = day }
                                    }
                                }
                            }
                            "Thống kê" -> Text("Báo cáo kinh doanh", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        if (currentTab != "Lịch booking" && currentTab != "Thống kê") {
                            IconButton(
                                onClick = {
                                    when (currentTab) {
                                        "Tiệm" -> { shopToEdit = null; showAddShopDialog = true }
                                        "Tài khoản" -> { userToEdit = null; showAddUserDialog = true }
                                        "Dịch vụ" -> { serviceToEdit = null; showAddServiceDialog = true }
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
                        val filteredShops = shops.filter {
                            it.name.contains(searchQuery, ignoreCase = true) || it.address.contains(searchQuery, ignoreCase = true)
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filteredShops, key = { it.id }) { shop ->
<<<<<<< HEAD
                                ShopCard(
                                    shop,
                                    onEdit = {
                                        viewModel.shopToEdit.value = shop
                                        viewModel.showAddShopDialog.value = true
                                    },
                                    onDelete = { viewModel.itemToDelete.value = shop })
=======
                                ShopCard(shop, onEdit = { shopToEdit = shop; showAddShopDialog = true }, onDelete = { itemToDelete = shop })
>>>>>>> feature/admin
                            }
                        }
                    }
                    "Tài khoản" -> {
<<<<<<< HEAD
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


=======
                        val filtered = if (selectedUserFilter == "Tất cả") users 
                                      else users.filter {
                                          when(selectedUserFilter) {
                                              "KH" -> it.role == "customer"
                                              "NV" -> it.role == "employee"
                                              "QL" -> it.role == "manager"
                                              else -> true
                                          }
                                      }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { user ->
                                UserCard(user, onEdit = { userToEdit = user; showAddUserDialog = true }, onDelete = { itemToDelete = user })
                            }
                        }
                    }
>>>>>>> feature/admin
                    "Dịch vụ" -> {
                        val shopServices = services.filter {
                            it.shopId == selectedShopForService?.id &&
                            it.name.contains(searchQuery, ignoreCase = true)
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(shopServices, key = { it.id }) { service ->
<<<<<<< HEAD
                                ServiceCard(
                                    service,
                                    onEdit = {
                                        viewModel.serviceToEdit.value = service;
                                        viewModel.showAddServiceDialog.value = true
                                    },
                                    onDelete = { viewModel.itemToDelete.value = service })
=======
                                ServiceCard(service, onEdit = { serviceToEdit = service; showAddServiceDialog = true }, onDelete = { itemToDelete = service })
>>>>>>> feature/admin
                            }
                        }
                    }
                    "Lịch booking" -> {
                        val filtered = if (selectedDateFilter == "Tất cả") bookings 
                                      else bookings.filter { it.dateTime.contains(selectedDateFilter) }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(filtered, key = { it.id }) { booking ->
                                BookingCard(
                                    booking = booking,
                                    onComplete = { /* Handle complete */ },
                                    onCancel = { /* Handle cancel */ },
                                    onDelete = { itemToDelete = booking }
                                )
                            }
                        }
                    }
                    "Thống kê" -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Thống kê (chưa triển khai)", color = Color.Gray) }
                    }
                }
            }
        }
    }

    // Local dialog view handling
    // Delete confirmation
    itemToDelete?.let { item ->
<<<<<<< HEAD
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
=======
        ConfirmDeleteDialog(title = "Xóa", message = "Xóa mục này?", onDismiss = { itemToDelete = null }) {
            // perform delete on local lists
            when (item) {
                is UserItem -> users.remove(item)
                is ServiceItem -> services.remove(item)
                is Shop -> shops.remove(item)
                is BookingItem -> bookings.remove(item)
            }
            itemToDelete = null
        }
    }

    if (showAddUserDialog) {
        // AdminDialogs.AddEditUserDialog expects onConfirm with (name, email, phone, password, role)
        AddEditUserDialog(userToEdit, onDismiss = { showAddUserDialog = false }) { n, e, p, pw, r ->
            if (userToEdit == null) users.add(UserItem(id = System.currentTimeMillis().toString(), name = n, email = e, phone = p, password = pw, role = r))
            else {
                val idx = users.indexOfFirst { it.id == userToEdit!!.id }
                if (idx >= 0) users[idx] = users[idx].copy(name = n, email = e, phone = p, password = pw, role = r)
            }
            showAddUserDialog = false
>>>>>>> feature/admin
        }
    }

    if (showAddServiceDialog) {
        AddEditServiceDialog(serviceToEdit, onDismiss = { showAddServiceDialog = false }) { n, d, p ->
            if (serviceToEdit == null) services.add(ServiceItem(id = System.currentTimeMillis().toString(), name = n, duration = d, price = p, shopId = selectedShopForService?.id ?: ""))
            else {
                val idx = services.indexOfFirst { it.id == serviceToEdit!!.id }
                if (idx >= 0) services[idx] = services[idx].copy(name = n, duration = d, price = p)
            }
            showAddServiceDialog = false
        }
    }

    if (showAddShopDialog) {
<<<<<<< HEAD
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

=======
        // AdminDialogs.AddEditShopDialog expects rating as Double
        AddEditShopDialog(shopToEdit, onDismiss = { showAddShopDialog = false }) { n, a, p, pr, rDouble, i ->
            if (shopToEdit == null) shops.add(Shop(id = System.currentTimeMillis().toString(), name = n, address = a, phone = p, priceRange = pr, rating = rDouble, imageUrl = i))
            else {
                val idx = shops.indexOfFirst { it.id == shopToEdit!!.id }
                if (idx >= 0) shops[idx] = shops[idx].copy(name = n, address = a, phone = p, priceRange = pr, rating = rDouble, imageUrl = i)
            }
            showAddShopDialog = false
        }
    }
}

// Note: ViewDialogs that relied on AdminViewModel was removed to keep this file self-contained.

@Composable
fun HeaderSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Admin Dashboard", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Quản lý hệ thống", color = Color.Gray, fontSize = 14.sp)
        }
        IconButton(onClick = { /* TODO: logout */ }) { Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = Color.White) }
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

@Composable
fun SearchBarCustom(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Tìm kiếm...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun FilterChipCustom(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
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
fun ShopCard(shop: Shop, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).background(Color.DarkGray, RoundedCornerShape(12.dp)))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(shop.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(shop.address ?: "", color = Color.Gray, fontSize = 12.sp, maxLines = 1)
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
            Box(modifier = Modifier.size(40.dp).background(Color.DarkGray, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.White)
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
fun BookingCard(booking: BookingItem, onComplete: () -> Unit, onCancel: () -> Unit, onDelete: () -> Unit) {
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

// Dialogs are implemented in AdminDialogs.kt; use those implementations to avoid duplicates.
>>>>>>> feature/admin
