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
import com.example.barberapp.Model.types.BookingStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val listeners = mutableListOf<ListenerRegistration>()


    // --- State ---
    private val _currentTab = mutableStateOf("Tiệm")
    val currentTab: State<String> = _currentTab

    val users = mutableStateListOf<User>()
    val services = mutableStateListOf<Service>()
    val bookings = mutableStateListOf<Booking>()
    val shops = mutableStateListOf<Shop>()

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedShopForService = mutableStateOf<Shop?>(null)
    val selectedShopForService: State<Shop?> = _selectedShopForService

    private val _selectedUserFilter = mutableStateOf("Tất cả")
    val selectedUserFilter: State<String> = _selectedUserFilter

    private val _selectedDateFilter = mutableStateOf("Tất cả")
    val selectedDateFilter: State<String> = _selectedDateFilter

    private val _selectedShopFilterForEmployee = mutableStateOf("Tất cả")
    val selectedShopFilterForEmployee: State<String> = _selectedShopFilterForEmployee
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
        // Hủy listener cũ trước khi đăng ký mới
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
        listeners += db.collection("bookings").addSnapshotListener { v, e ->
            if (e != null) return@addSnapshotListener
            v?.let {
                bookings.clear()
                bookings.addAll(it.documents.mapNotNull { d ->
                    d.toObject(Booking::class.java)?.copy(id = d.id)
                })
                calculateStats() // tính lại stats mỗi khi bookings thay đổi
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listeners.forEach { it.remove() }
    }

    // --- Actions ---
    fun setCurrentTab(tab: String) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedShopForService(shop: Shop) {
        _selectedShopForService.value = shop
    }

    fun setSelectedUserFilter(filter: String) {
        _selectedUserFilter.value = filter
    }

    fun setSelectedDateFilter(filter: String) {
        _selectedDateFilter.value = filter
    }
    fun setStatsTimeRange(range: String) { statsTimeRange.value = range; calculateStats() }
//    fun updateShopFilterForEmployee(id: String) { selectedShopFilterForEmployee.value = id }

    fun confirmBooking(bookingId: String) {
        db.collection("bookings").document(bookingId)
            .update("status", BookingStatus.Completed.name)
    }

    fun cancelBooking(bookingId: String) {
        db.collection("bookings").document(bookingId)
            .update("status", BookingStatus.Cancelled.name)
    }

    fun deleteBooking(bookingId: String) {
        db.collection("bookings").document(bookingId).delete()
        itemToDelete.value = null
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

    fun deleteItem(item: Any) {
        when (item) {
            is User -> db.collection("users").document(item.id).delete()

            is Shop -> {
                val shopId = item.id

                // 1. Tìm kiếm tất cả dịch vụ thuộc về Shop này
                db.collection("services").whereEqualTo("shopId", shopId).get()
                    .addOnSuccessListener { serviceSnapshot ->

                        // 2. Tìm kiếm tất cả thợ (barbers) thuộc về Shop này (để tránh sót dữ liệu rác)
                        db.collection("barbers").whereEqualTo("shopId", shopId).get()
                            .addOnSuccessListener { barberSnapshot ->

                                // Khởi tạo một WriteBatch để gom tất cả các lệnh xóa lại chạy một lượt
                                val batch = db.batch()

                                // Thêm lệnh xóa toàn bộ Dịch vụ của Shop này vào batch
                                for (doc in serviceSnapshot.documents) {
                                    batch.delete(doc.reference)
                                }

                                // Thêm lệnh xóa toàn bộ Thợ của Shop này vào batch
                                for (doc in barberSnapshot.documents) {
                                    batch.delete(doc.reference)
                                }

                                // Cuối cùng, thêm lệnh xóa chính tài liệu Shop đó vào batch
                                val shopRef = db.collection("shops").document(shopId)
                                batch.delete(shopRef)

                                // Thực thi xóa đồng loạt (Atomic Operation)
                                batch.commit().addOnSuccessListener {
                                    // Sau khi xóa thành công toàn bộ dưới DB, gọi các hàm nạp lại dữ liệu để cập nhật UI
//                                    fetchData()
                                }
                            }
                    }
            }

            // Đoạn xóa dịch vụ lẻ lẻ do Admin chủ động chọn xóa
            is Service -> db.collection("services").document(item.id).delete()
        }
        itemToDelete.value = null
    }

    fun saveUser(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String,
        shopId: String
    ) {
        val colorHex = when (role) {
            "employee" -> "#4CAF50"; "manager" -> "#9C27B0"; else -> "#2196F3"
        }
        val data = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "password" to password,
            "role" to role,
            "roleColorHex" to colorHex,
            "shopId" to if (role == "employee") shopId else ""
        )
        val task = if (userToEdit.value == null) db.collection("users")
            .add(data) else db.collection("users").document(userToEdit.value!!.id).set(data)
//        if (onSuccess.isSuccessful) fetchData()
        task.addOnSuccessListener { fetchData() }
        showAddUserDialog.value = false
        userToEdit.value=null
    }

    fun saveService(name: String, duration: String, price: String) {
        val currentShopId = _selectedShopForService.value?.id ?: ""
        val data = hashMapOf(
            "name" to name,
            "duration" to duration,
            "price" to (price.filter { char -> char.isDigit() }
                .toIntOrNull() ?: 0),
            "shopId" to currentShopId
        )
        val task = if (serviceToEdit.value == null) db.collection("services").add(data)
        else db.collection("services").document(serviceToEdit.value!!.id).set(data)
//        if (onSuccess.isSuccessful) fetchData()
        task.addOnSuccessListener { fetchData() }
        showAddServiceDialog.value = false
        serviceToEdit.value = null
    }

    fun saveShop(
        shopIdToEdit: String?,
        name: String,
        address: String,
        phone: String,
        priceRange: String,
        rating: Double,
        imageUrl: String,
        servicesList: List<Service>,
        barbersList: List<Employee>
    ) {
        val batch = db.batch()

        val shopDocRef = if (shopIdToEdit.isNullOrBlank()) db.collection("shops").document()
        else db.collection("shops").document(shopIdToEdit)
        val finalShopId = shopDocRef.id

        val shopData = hashMapOf(
            "name" to name,
            "address" to address,
            "phone" to phone,
            "priceRange" to priceRange,
            "rating" to rating,
            "imageUrl" to imageUrl
        )
        batch.set(shopDocRef, shopData)
        val servicesToSave = if (servicesList.isEmpty() && shopIdToEdit.isNullOrBlank()) {
            getDefaultServices(finalShopId)
        } else {
            servicesList
        }

        servicesToSave.forEach { service ->
            val serviceRef = if (service.id.isBlank()) db.collection("services").document()
            else db.collection("services").document(service.id)

            val serviceData = hashMapOf(
                "name" to service.name,
                "duration" to service.duration,
                "price" to service.price,
                "shopId" to finalShopId // <-- Gắn chặt Khóa ngoại ở đây
            )
            batch.set(serviceRef, serviceData)
        }

        barbersList.forEach { barber ->
            val barberRef = if (barber.id.isBlank()) db.collection("barbers").document()
            else db.collection("barbers").document(barber.id)

            val barberData = hashMapOf(
                "name" to barber.name,
                "avatarUrl" to barber.avatarUrl,
                "rating" to barber.rating,
                "shopId" to finalShopId
            )
            batch.set(barberRef, barberData)
        }

        batch.commit().addOnSuccessListener {
//            fetchData()
        }
        showAddShopDialog.value = false
    }

    fun updateShopFilterForEmployee(shopId: String) {
        _selectedShopFilterForEmployee.value = shopId
    }

    private fun getDefaultServices(shopId: String): List<Service> {
        return listOf(
            Service(name = "Hair Cut", duration = "30 phút", price = 100000, shopId = shopId),
            Service(name = "Beard Shave", duration = "20 phút", price = 50000, shopId = shopId),
            Service(name = "Hair Styling", duration = "45 phút", price = 150000, shopId = shopId),
            Service(name = "Premium Styling", duration = "60 phút", price = 250000, shopId = shopId)
        )
    }
}