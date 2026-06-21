package com.example.barberapp.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.Model.entities.AuditLog
import com.example.barberapp.Model.entities.SystemConfig
import com.example.barberapp.Model.types.BookingStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AdminViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val listeners = mutableListOf<ListenerRegistration>()

    private val _currentTab = mutableStateOf(savedStateHandle.get<String>("currentTab") ?: "Tiệm")
    val currentTab: State<String> = _currentTab

    private val _searchQuery = mutableStateOf(savedStateHandle.get<String>("searchQuery") ?: "")
    val searchQuery: State<String> = _searchQuery

    private val _selectedUserFilter = mutableStateOf(savedStateHandle.get<String>("userFilter") ?: "Tất cả")
    val selectedUserFilter: State<String> = _selectedUserFilter

    // 4. Phân loại theo thời gian: "Hôm nay", "Sắp tới", "Tất cả"
    private val _selectedDateFilter = mutableStateOf(savedStateHandle.get<String>("dateFilter") ?: "Hôm nay")
    val selectedDateFilter: State<String> = _selectedDateFilter

    // 1. Lọc trạng thái: "Tất cả", "Pending", "Completed", "Cancelled"
    private val _selectedBookingStatus = mutableStateOf(savedStateHandle.get<String>("bookingStatus") ?: "Tất cả")
    val selectedBookingStatus: State<String> = _selectedBookingStatus

    private val _statsTimeRange = mutableStateOf(savedStateHandle.get<String>("statsRange") ?: "Tháng")
    val statsTimeRange: State<String> = _statsTimeRange

    val users = mutableStateListOf<User>()
    val services = mutableStateListOf<Service>()
    val bookings = mutableStateListOf<Booking>()
    val shops = mutableStateListOf<Shop>()
    val auditLogs = mutableStateListOf<AuditLog>()
    
    private val _systemConfig = mutableStateOf(SystemConfig())
    val systemConfig: State<SystemConfig> = _systemConfig

    private val _selectedShopForService = mutableStateOf<Shop?>(null)
    val selectedShopForService: State<Shop?> = _selectedShopForService

    private val _selectedShopFilterForEmployee = mutableStateOf("Tất cả")
    val selectedShopFilterForEmployee: State<String> = _selectedShopFilterForEmployee
    
    val totalRevenue = mutableStateOf(0)
    val totalBookingsCount = mutableStateOf(0)
    val avgRevenuePerBooking = mutableStateOf(0)
    val avgBookingsPerStaff = mutableStateOf(0.0)
    val completionRate = mutableStateOf(0)
    val popularServices = mutableStateListOf<Pair<String, Int>>()
    val staffPerformance = mutableStateListOf<Pair<String, Int>>()

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
        listeners.forEach { it.remove() }
        listeners.clear()

        listeners += db.collection("users").addSnapshotListener { v, e ->
            if (e != null) return@addSnapshotListener
            v?.let { users.clear(); users.addAll(it.documents.mapNotNull { d ->
                d.toObject(User::class.java)?.copy(id = d.id)
            }) }
        }
        listeners += db.collection("services").addSnapshotListener { v, e ->
            if (e != null) return@addSnapshotListener
            v?.let { services.clear(); services.addAll(it.documents.mapNotNull { d ->
                d.toObject(Service::class.java)?.copy(id = d.id)
            }) }
        }
        listeners += db.collection("shops").addSnapshotListener { v, e ->
            if (e != null) return@addSnapshotListener
            v?.let {
                shops.clear()
                val fetched = it.documents.mapNotNull { d ->
                    d.toObject(Shop::class.java)?.copy(id = d.id)
                }
                shops.addAll(fetched)
                if (_selectedShopForService.value == null && fetched.isNotEmpty())
                    _selectedShopForService.value = fetched.first()
            }
        }
        listeners += db.collection("bookings")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { v, e ->
                if (e != null) return@addSnapshotListener
                v?.let {
                    bookings.clear()
                    bookings.addAll(it.documents.mapNotNull { d ->
                        d.toObject(Booking::class.java)?.copy(id = d.id)
                    })
                    calculateStats() 
                }
            }
        listeners += db.collection("audit_logs")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { v, e ->
                if (e != null) return@addSnapshotListener
                v?.let {
                    auditLogs.clear()
                    auditLogs.addAll(it.documents.mapNotNull { d ->
                        d.toObject(AuditLog::class.java)?.copy(id = d.id)
                    })
                }
            }
        
        listeners += db.collection("config").document("system").addSnapshotListener { v, e ->
            if (e != null) return@addSnapshotListener
            v?.toObject(SystemConfig::class.java)?.let {
                _systemConfig.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listeners.forEach { it.remove() }
    }

    private fun logAction(action: String, details: String) {
        val adminId = auth.currentUser?.uid ?: "unknown"
        val adminName = users.find { it.id == adminId }?.name ?: "Admin"
        val log = AuditLog(
            adminId = adminId,
            adminName = adminName,
            action = action,
            details = details,
            timestamp = System.currentTimeMillis()
        )
        db.collection("audit_logs").add(log)
    }

    fun setCurrentTab(tab: String) { 
        _currentTab.value = tab 
        savedStateHandle["currentTab"] = tab
    }
    
    fun setSearchQuery(query: String) { 
        _searchQuery.value = query 
        savedStateHandle["searchQuery"] = query
    }

    fun setSelectedUserFilter(filter: String) { 
        _selectedUserFilter.value = filter 
        savedStateHandle["userFilter"] = filter
    }

    fun setSelectedDateFilter(filter: String) { 
        _selectedDateFilter.value = filter 
        savedStateHandle["dateFilter"] = filter
    }

    fun setSelectedBookingStatus(status: String) {
        _selectedBookingStatus.value = status
        savedStateHandle["bookingStatus"] = status
    }

    fun setStatsTimeRange(range: String) { 
        _statsTimeRange.value = range 
        savedStateHandle["statsRange"] = range
        calculateStats() 
    }

    fun setSelectedShopForService(shop: Shop) { _selectedShopForService.value = shop }

    fun confirmBooking(bookingId: String) {
        db.collection("bookings").document(bookingId).update("status", BookingStatus.Completed.name)
            .addOnSuccessListener { logAction("Hoàn thành lịch hẹn", "ID: $bookingId") }
    }

    fun cancelBooking(bookingId: String) {
        db.collection("bookings").document(bookingId).update("status", BookingStatus.Cancelled.name)
            .addOnSuccessListener { logAction("Hủy lịch hẹn", "ID: $bookingId") }
    }

    fun deleteBooking(bookingId: String) {
        db.collection("bookings").document(bookingId).delete()
            .addOnSuccessListener { logAction("Xóa lịch hẹn", "ID: $bookingId") }
        itemToDelete.value = null
    }

    fun calculateStats() {
        val now = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val timeFilteredBookings = bookings.filter { booking ->
            try {
                val date = sdf.parse(booking.bookingDate) ?: return@filter false
                val cal = Calendar.getInstance().apply { time = date }
                
                when (_statsTimeRange.value) {
                    "Hôm nay" -> {
                        cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                        cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
                    }
                    "Tuần" -> {
                        cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                        cal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)
                    }
                    "Tháng" -> {
                        cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                        cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    }
                    "Năm" -> {
                        cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                    }
                    else -> true
                }
            } catch (e: Exception) { false }
        }

        val completedBookings = timeFilteredBookings.filter { it.status == BookingStatus.Completed }
        
        totalBookingsCount.value = completedBookings.size
        totalRevenue.value = completedBookings.sumOf { it.totalPrice.toInt() }
        
        avgRevenuePerBooking.value = if (completedBookings.isNotEmpty()) totalRevenue.value / completedBookings.size else 0

        val staffCount = users.count { it.role == "employee" }
        avgBookingsPerStaff.value = if (staffCount > 0) completedBookings.size.toDouble() / staffCount else 0.0

        completionRate.value = if (timeFilteredBookings.isNotEmpty()) {
            (completedBookings.size * 100) / timeFilteredBookings.size
        } else 0

        val serviceMap = mutableMapOf<String, Int>()
        completedBookings.forEach { b ->
            b.services.forEach { s ->
                serviceMap[s.name] = serviceMap.getOrDefault(s.name, 0) + 1
            }
        }
        popularServices.clear()
        popularServices.addAll(serviceMap.toList().sortedByDescending { it.second }.take(5))

        val staffMap = mutableMapOf<String, Int>()
        completedBookings.forEach { b ->
            staffMap[b.barberName] = staffMap.getOrDefault(b.barberName, 0) + 1
        }
        staffPerformance.clear()
        staffPerformance.addAll(staffMap.toList().sortedByDescending { it.second })
    }

    fun refreshData() { fetchData() }

    fun deleteItem(item: Any) {
        when (item) {
            is User -> db.collection("users").document(item.id).delete().addOnSuccessListener { logAction("Xóa tài khoản", "${item.name} (${item.role})") }
            is Shop -> {
                val shopId = item.id
                val batch = db.batch()
                db.collection("services").whereEqualTo("shopId", shopId).get().addOnSuccessListener { s ->
                    s.forEach { batch.delete(it.reference) }
                    db.collection("barbers").whereEqualTo("shopId", shopId).get().addOnSuccessListener { b ->
                        b.forEach { batch.delete(it.reference) }
                        batch.delete(db.collection("shops").document(shopId))
                        batch.commit().addOnSuccessListener { logAction("Xóa tiệm", item.name) }
                    }
                }
            }
            is Service -> db.collection("services").document(item.id).delete().addOnSuccessListener { logAction("Xóa dịch vụ", item.name) }
        }
        itemToDelete.value = null
    }

    fun saveUser(name: String, email: String, phone: String, pw: String, role: String, shopId: String) {
        val colorHex = when (role) { "employee" -> "#4CAF50"; "manager" -> "#9C27B0"; else -> "#2196F3" }
        val data = hashMapOf(
            "name" to name, "email" to email, "phone" to phone, "password" to pw,
            "role" to role, "roleColorHex" to colorHex, "shopId" to if (role == "employee") shopId else ""
        )
        val isNew = userToEdit.value == null
        val task = if (isNew) db.collection("users").add(data) 
                   else db.collection("users").document(userToEdit.value!!.id).set(data)
        
        task.addOnSuccessListener { 
            fetchData()
            logAction(if (isNew) "Thêm tài khoản" else "Cập nhật tài khoản", "$name ($role)")
        }
        showAddUserDialog.value = false
        userToEdit.value = null
    }

    fun saveService(name: String, duration: String, price: String) {
        val currentShopId = _selectedShopForService.value?.id ?: ""
        val data = hashMapOf(
            "name" to name, "duration" to duration,
            "price" to (price.filter { it.isDigit() }.toIntOrNull() ?: 0),
            "shopId" to currentShopId
        )
        val isNew = serviceToEdit.value == null
        val task = if (isNew) db.collection("services").add(data)
                   else db.collection("services").document(serviceToEdit.value!!.id).set(data)
        
        task.addOnSuccessListener { 
            fetchData()
            logAction(if (isNew) "Thêm dịch vụ" else "Cập nhật dịch vụ", name)
        }
        showAddServiceDialog.value = false
        serviceToEdit.value = null
    }

    fun saveShop(shopId: String?, name: String, addr: String, ph: String, pr: String, r: Double, img: String, svs: List<Service>, barbs: List<Employee>) {
        val batch = db.batch()
        val shopRef = if (shopId.isNullOrBlank()) db.collection("shops").document() else db.collection("shops").document(shopId)
        val finalId = shopRef.id
        val isNew = shopId.isNullOrBlank()
        
        batch.set(shopRef, hashMapOf("name" to name, "address" to addr, "phone" to ph, "priceRange" to pr, "rating" to r, "imageUrl" to img))
        
        (if (svs.isEmpty() && isNew) getDefaultServices(finalId) else svs).forEach { s ->
            val sRef = if (s.id.isBlank()) db.collection("services").document() else db.collection("services").document(s.id)
            batch.set(sRef, hashMapOf("name" to s.name, "duration" to s.duration, "price" to s.price, "shopId" to finalId))
        }
        barbs.forEach { b ->
            val bRef = if (b.id.isBlank()) db.collection("barbers").document() else db.collection("barbers").document(b.id)
            batch.set(bRef, hashMapOf("name" to b.name, "avatarUrl" to b.avatarUrl, "rating" to b.rating, "shopId" to finalId))
        }
        batch.commit().addOnSuccessListener { 
            fetchData()
            logAction(if (isNew) "Thêm tiệm" else "Cập nhật tiệm", name)
        }
        showAddShopDialog.value = false
    }

    fun updateShopFilterForEmployee(shopId: String) { _selectedShopFilterForEmployee.value = shopId }

    fun updateSystemConfig(config: SystemConfig) {
        db.collection("config").document("system").set(config)
            .addOnSuccessListener { logAction("Cập nhật hệ thống", "Version: ${config.appVersion}") }
    }

    private fun getDefaultServices(shopId: String) = listOf(
        Service(name = "Hair Cut", duration = "30 phút", price = 100000, shopId = shopId),
        Service(name = "Beard Shave", duration = "20 phút", price = 50000, shopId = shopId),
        Service(name = "Hair Styling", duration = "45 phút", price = 150000, shopId = shopId)
    )
}
