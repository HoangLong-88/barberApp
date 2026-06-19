package com.example.barberapp.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.example.barberapp.Model.entities.Booking
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // --- State Cơ Bản ---
    private val _currentTab = mutableStateOf("Tiệm")
    val currentTab: State<String> = _currentTab

    val users = mutableStateListOf<User>()
    val services = mutableStateListOf<Service>()
    val bookings = mutableStateListOf<Booking>()
    val shops = mutableStateListOf<Shop>()

    val searchQuery = mutableStateOf("")
    val selectedShopForService = mutableStateOf<Shop?>(null)
    val selectedUserFilter = mutableStateOf("Tất cả")
    val selectedDateFilter = mutableStateOf("Tất cả")
    val selectedShopFilterForEmployee = mutableStateOf("Tất cả")

    // --- State Thống Kê ---
    val totalRevenue = mutableStateOf(0)
    val totalBookingsCount = mutableStateOf(0)
    val avgRevenuePerBooking = mutableStateOf(0)
    val avgBookingsPerStaff = mutableStateOf(0.0)
    val completionRate = mutableStateOf(0)
    val popularServices = mutableStateListOf<Pair<String, Int>>()
    val staffPerformance = mutableStateListOf<Pair<String, Int>>()
    val statsTimeRange = mutableStateOf("Tháng")

    // Dialog States
    val showAddUserDialog = mutableStateOf(false)
    val userToEdit = mutableStateOf<User?>(null)
    val showAddServiceDialog = mutableStateOf(false)
    val serviceToEdit = mutableStateOf<Service?>(null)
    val showAddShopDialog = mutableStateOf(false)
    val shopToEdit = mutableStateOf<Shop?>(null)
    val itemToDelete = mutableStateOf<Any?>(null)

    init {
        fetchData()
    }

    private fun fetchData() {
        db.collection("users").addSnapshotListener { v, _ ->
            v?.let {
                users.clear()
                users.addAll(it.documents.mapNotNull { d -> d.toObject(User::class.java)?.copy(id = d.id) })
                calculateStats()
            }
        }
        db.collection("services").addSnapshotListener { v, _ ->
            v?.let {
                services.clear()
                services.addAll(it.documents.mapNotNull { d -> d.toObject(Service::class.java)?.copy(id = d.id) })
            }
        }
        db.collection("shops").addSnapshotListener { v, _ ->
            v?.let {
                shops.clear()
                val fetched = it.documents.mapNotNull { d -> d.toObject(Shop::class.java)?.copy(id = d.id) }
                shops.addAll(fetched)
                if (selectedShopForService.value == null && fetched.isNotEmpty()) selectedShopForService.value = fetched.first()
            }
        }
        db.collection("bookings").addSnapshotListener { v, _ ->
            v?.let {
                bookings.clear()
                bookings.addAll(it.documents.mapNotNull { d -> d.toObject(Booking::class.java)?.copy(id = d.id) })
                calculateStats()
            }
        }
    }

    fun calculateStats() {
        // 1. Tổng doanh thu & Số lượng
        val completedBookings = bookings 
        totalBookingsCount.value = completedBookings.size
        totalRevenue.value = completedBookings.sumOf { it.totalPrice.toInt() }

        // 2. Trung bình
        avgRevenuePerBooking.value = if (totalBookingsCount.value > 0) totalRevenue.value / totalBookingsCount.value else 0
        
        val staffCount = users.count { it.role == "employee" }
        avgBookingsPerStaff.value = if (staffCount > 0) totalBookingsCount.value.toDouble() / staffCount else 0.0

        // 3. Tỷ lệ hoàn thành
        completionRate.value = if (bookings.isNotEmpty()) (completedBookings.size * 100) / bookings.size else 0

        // 4. Dịch vụ phổ biến
        val serviceMap = mutableMapOf<String, Int>()
        completedBookings.forEach { b ->
            b.services.forEach { s ->
                serviceMap[s.name] = serviceMap.getOrDefault(s.name, 0) + 1
            }
        }
        popularServices.clear()
        popularServices.addAll(serviceMap.toList().sortedByDescending { it.second }.take(5))

        // 5. Hiệu suất nhân viên
        val staffMap = mutableMapOf<String, Int>()
        completedBookings.forEach { b ->
            staffMap[b.barberName] = staffMap.getOrDefault(b.barberName, 0) + 1
        }
        staffPerformance.clear()
        staffPerformance.addAll(staffMap.toList().sortedByDescending { it.second })
    }

    fun refreshData() { fetchData() }
    fun setStatsTimeRange(range: String) { statsTimeRange.value = range; calculateStats() }
    
    fun setCurrentTab(tab: String) { _currentTab.value = tab }
    fun setSearchQuery(query: String) { searchQuery.value = query }
    fun setSelectedShopForService(shop: Shop) { selectedShopForService.value = shop }
    fun setSelectedUserFilter(f: String) { selectedUserFilter.value = f }
    fun setSelectedDateFilter(f: String) { selectedDateFilter.value = f }
    fun updateShopFilterForEmployee(id: String) { selectedShopFilterForEmployee.value = id }

    fun deleteItem(item: Any) {
        when (item) {
            is User -> db.collection("users").document(item.id).delete()
            is Service -> db.collection("services").document(item.id).delete()
            is Shop -> db.collection("shops").document(item.id).delete()
        }
        itemToDelete.value = null
    }

    fun saveUser(n: String, e: String, p: String, pw: String, r: String, sId: String) {
        val data = hashMapOf("name" to n, "email" to e, "phone" to p, "password" to pw, "role" to r, "shopId" to sId)
        db.collection("users").add(data).addOnCompleteListener { showAddUserDialog.value = false }
    }

    fun saveService(n: String, d: String, p: String) {
        val data = hashMapOf("name" to n, "duration" to d, "price" to (p.filter { it.isDigit() }.toIntOrNull() ?: 0), "shopId" to (selectedShopForService.value?.id ?: ""))
        db.collection("services").add(data).addOnCompleteListener { showAddServiceDialog.value = false }
    }

    fun saveShop(id: String?, n: String, a: String, ph: String, pr: String, r: Double, i: String, servicesList: List<Service>, barbersList: List<Employee>) {
        val data = hashMapOf("name" to n, "address" to a, "phone" to ph, "priceRange" to pr, "rating" to r, "imageUrl" to i)
        val ref = if (id.isNullOrBlank()) db.collection("shops").document() else db.collection("shops").document(id)
        ref.set(data).addOnCompleteListener { showAddShopDialog.value = false }
    }
}
