package com.example.barberapp.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.barberapp.Model.entities.BookingItem
import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // --- State ---
    private val _currentTab = mutableStateOf("Tiệm")
    val currentTab: State<String> = _currentTab

    val users = mutableStateListOf<User>()
    val services = mutableStateListOf<Service>()
    val bookings = mutableStateListOf<BookingItem>()
    val shops = mutableStateListOf<Shop>()

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedUserFilter = mutableStateOf("Tất cả")
    val selectedUserFilter: State<String> = _selectedUserFilter

    private val _selectedDateFilter = mutableStateOf("Tất cả")
    val selectedDateFilter: State<String> = _selectedDateFilter

<<<<<<< HEAD
    private val _selectedShopFilterForEmployee = mutableStateOf("Tất cả")
    val selectedShopFilterForEmployee: State<String> = _selectedShopFilterForEmployee
=======
    private val _selectedShopForService = mutableStateOf<Shop?>(null)
    val selectedShopForService: State<Shop?> = _selectedShopForService

    // Admin Profile State
    private val _currentAdmin = mutableStateOf<UserItem?>(null)
    val currentAdmin: State<UserItem?> = _currentAdmin

    // Statistics State
    private val _totalRevenue = mutableStateOf(0L)
    val totalRevenue: State<Long> = _totalRevenue

    private val _totalBookingsCount = mutableStateOf(0)
    val totalBookingsCount: State<Int> = _totalBookingsCount

    private val _popularServices = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val popularServices: State<List<Pair<String, Int>>> = _popularServices

    private val _staffPerformance = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val staffPerformance: State<List<Pair<String, Int>>> = _staffPerformance
>>>>>>> feature/admin

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
        // Listen Users
<<<<<<< HEAD
        db.collection("users").addSnapshotListener { v, e ->
            if (e != null) return@addSnapshotListener
            v?.let {
                users.clear()
                users.addAll(it.documents.mapNotNull { d ->
                    try {
                        d.toObject(User::class.java)?.copy(id = d.id)
                    } catch (ex: Exception) {
                        null
                    }
                })
            }
        }
        // Listen Services
        db.collection("services").addSnapshotListener { v, e ->
            if (e != null) return@addSnapshotListener
            v?.let {
                services.clear()
                services.addAll(it.documents.mapNotNull { d ->
                    try {
                        d.toObject(Service::class.java)?.copy(id = d.id)
                    } catch (ex: Exception) {
                        null
                    }
                })
            }
        }
        // Listen Shops
        db.collection("shops").addSnapshotListener { v, e ->
            if (e != null) return@addSnapshotListener
            v?.let {
                shops.clear()
                val fetchedShops =
                    it.documents.mapNotNull { d ->
                        try {
                            d.toObject(Shop::class.java)?.copy(id = d.id)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                shops.addAll(fetchedShops)
                if (_selectedShopForService.value == null && fetchedShops.isNotEmpty()) {
                    _selectedShopForService.value = fetchedShops.first()
                }
            }
        }
=======
        db.collection("users").addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                users.clear()
                for (doc in it.documents) {
                    val u = doc.toObject(UserItem::class.java)?.copy(id = doc.id)
                    if (u != null) users.add(u)
                }
            }
        }

        // Listen Services
        db.collection("services").addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                services.clear()
                for (doc in it.documents) {
                    val s = doc.toObject(ServiceItem::class.java)?.copy(id = doc.id)
                    if (s != null) services.add(s)
                }
            }
        }

        // Listen Shops
        db.collection("shops").addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                shops.clear()
                val fetched = mutableListOf<Shop>()
                for (doc in it.documents) {
                    val sh = doc.toObject(Shop::class.java)?.copy(id = doc.id)
                    if (sh != null) fetched.add(sh)
                }
                shops.addAll(fetched)
                if (_selectedShopForService.value == null && fetched.isNotEmpty()) {
                    _selectedShopForService.value = fetched.first()
                }
            }
        }

        // Clear bookings (or implement fetch if you have bookings collection)
        bookings.clear()

        // Update statistics
        updateStatistics()
    }

    private fun updateStatistics() {
        // Calculate total bookings
        _totalBookingsCount.value = bookings.size

        // Calculate popular services
        val serviceCount = mutableMapOf<String, Int>()
        for (booking in bookings) {
            serviceCount[booking.serviceName] = (serviceCount[booking.serviceName] ?: 0) + 1
        }
        _popularServices.value = serviceCount.toList().sortedByDescending { it.second }.take(5)

        // Calculate staff performance
        val staffCount = mutableMapOf<String, Int>()
        for (booking in bookings) {
            staffCount[booking.barberName] = (staffCount[booking.barberName] ?: 0) + 1
        }
        _staffPerformance.value = staffCount.toList().sortedByDescending { it.second }

        // Calculate total revenue (mock: 100000 per booking)
        _totalRevenue.value = (bookings.size * 100000L)
>>>>>>> feature/admin
    }

    // --- Actions ---
    fun setCurrentTab(tab: String) { _currentTab.value = tab }
    fun setSearchQuery(q: String) { _searchQuery.value = q }
    fun setSelectedUserFilter(f: String) { _selectedUserFilter.value = f }
    fun setSelectedDateFilter(f: String) { _selectedDateFilter.value = f }
    fun setSelectedShopForService(shop: Shop?) { _selectedShopForService.value = shop }

    fun deleteItem(item: Any) {
        when (item) {
<<<<<<< HEAD
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
                                    fetchData()
                                }
                            }
                    }
            }

            // Đoạn xóa dịch vụ lẻ lẻ do Admin chủ động chọn xóa
            is Service -> db.collection("services").document(item.id).delete()
=======
            is UserItem -> db.collection("users").document(item.id).delete()
            is ServiceItem -> db.collection("services").document(item.id).delete()
            is Shop -> db.collection("shops").document(item.id).delete()
            is BookingItem -> db.collection("bookings").document(item.id).delete()
>>>>>>> feature/admin
        }
        itemToDelete.value = null
    }

<<<<<<< HEAD
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
=======
    fun saveUser(name: String, email: String, phone: String, password: String, role: String) {
>>>>>>> feature/admin
        val data = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "password" to password,
<<<<<<< HEAD
            "role" to role,
            "roleColorHex" to colorHex,
            "shopId" to if (role == "employee") shopId else ""
=======
            "role" to role
>>>>>>> feature/admin
        )
        val onSuccess = if (userToEdit.value == null) db.collection("users")
            .add(data) else db.collection("users").document(userToEdit.value!!.id).set(data)
        if (onSuccess.isSuccessful) fetchData()
        showAddUserDialog.value = false
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
        val onSuccess = if (serviceToEdit.value == null) db.collection("services").add(data)
        else db.collection("services").document(serviceToEdit.value!!.id).set(data)
        if (onSuccess.isSuccessful) fetchData()
        showAddServiceDialog.value = false
    }

<<<<<<< HEAD
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
=======
    fun saveShop(name: String, address: String, phone: String, priceRange: String, rating: Double, imageUrl: String) {
        val data = hashMapOf(
>>>>>>> feature/admin
            "name" to name,
            "address" to address,
            "phone" to phone,
            "priceRange" to priceRange,
            "rating" to rating,
            "imageUrl" to imageUrl
        )
<<<<<<< HEAD
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
            fetchData()
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
=======
        if (shopToEdit.value == null) db.collection("shops").add(data)
        else db.collection("shops").document(shopToEdit.value!!.id).set(data)
        showAddShopDialog.value = false
    }

    fun logout() {
        // TODO: Implement real logout with Firebase Auth
        _currentAdmin.value = null
>>>>>>> feature/admin
    }
}