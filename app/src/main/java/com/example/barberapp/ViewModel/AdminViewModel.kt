package com.example.barberapp.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // --- State ---
    private val _currentTab = mutableStateOf("Tiệm")
    val currentTab: State<String> = _currentTab

    val users = mutableStateListOf<User>()
    val services = mutableStateListOf<Service>()
    val bookings = mutableStateListOf<Booking>()
    val shops = mutableStateListOf<Shop>()

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedUserFilter = mutableStateOf("Tất cả")
    val selectedUserFilter: State<String> = _selectedUserFilter

    private val _selectedDateFilter = mutableStateOf("Tất cả")
    val selectedDateFilter: State<String> = _selectedDateFilter

    private val _selectedShopForService = mutableStateOf<Shop?>(null)
    val selectedShopForService: State<Shop?> = _selectedShopForService

    // Admin Profile State
    private val _currentAdmin = mutableStateOf<User?>(null)
    val currentAdmin: State<User?> = _currentAdmin

    // Statistics - Date Range Filter
    private val _statsTimeRange = mutableStateOf("Tháng")
    val statsTimeRange: State<String> = _statsTimeRange

    // Statistics - Main metrics
    private val _totalRevenue = mutableStateOf(0L)
    val totalRevenue: State<Long> = _totalRevenue

    private val _totalBookingsCount = mutableStateOf(0)
    val totalBookingsCount: State<Int> = _totalBookingsCount

    private val _popularServices = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val popularServices: State<List<Pair<String, Int>>> = _popularServices

    private val _staffPerformance = mutableStateOf<List<Pair<String, Int>>>(emptyList())
    val staffPerformance: State<List<Pair<String, Int>>> = _staffPerformance

    // Statistics - Additional metrics
    private val _avgRevenuePerBooking = mutableStateOf(0L)
    val avgRevenuePerBooking: State<Long> = _avgRevenuePerBooking

    private val _avgBookingsPerStaff = mutableStateOf(0)
    val avgBookingsPerStaff: State<Int> = _avgBookingsPerStaff

    private val _completionRate = mutableStateOf(0)
    val completionRate: State<Int> = _completionRate

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

    // Statistics calculation happens in updateStatistics() below and in fetchData listener

    private fun fetchData() {
        // Listen Users
        db.collection("users").addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                users.clear()
                for (doc in it.documents) {
                    val u = doc.toObject(User::class.java)?.copy(id = doc.id)
                    if (u != null) users.add(u)
                }
            }
        }

        // Listen Services
        db.collection("services").addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                services.clear()
                for (doc in it.documents) {
                    val s = doc.toObject(Service::class.java)?.copy(id = doc.id)
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

        // Listen bookings
        db.collection("bookings").addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                bookings.clear()
                for (doc in it.documents) {
                    val b = try {
                        doc.toObject(Booking::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        null
                    }
                    if (b != null) bookings.add(b)
                }
                // update statistics whenever bookings change
                updateStatistics()
            }
        }
    }

    // --- Actions ---
    fun setCurrentTab(tab: String) { _currentTab.value = tab }
    fun setSearchQuery(q: String) { _searchQuery.value = q }
    fun setSelectedUserFilter(f: String) { _selectedUserFilter.value = f }
    fun setSelectedDateFilter(f: String) { _selectedDateFilter.value = f }
    fun setSelectedShopForService(shop: Shop?) { _selectedShopForService.value = shop }
    fun setStatsTimeRange(range: String) { _statsTimeRange.value = range; updateStatistics() }
    fun refreshData() { updateStatistics() }

    fun deleteItem(item: Any) {
        when (item) {
            is User -> db.collection("users").document(item.id).delete()
            is Service -> db.collection("services").document(item.id).delete()
            is Shop -> db.collection("shops").document(item.id).delete()
            is Booking -> db.collection("bookings").document(item.id).delete()
        }
        itemToDelete.value = null
    }

    fun saveUser(name: String, email: String, phone: String, password: String, role: String) {
        val data = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "password" to password,
            "role" to role
        )
        if (userToEdit.value == null) db.collection("users").add(data)
        else db.collection("users").document(userToEdit.value!!.id).set(data)
        showAddUserDialog.value = false
    }

    fun saveService(name: String, duration: String, price: Int) {
        val currentShopId = _selectedShopForService.value?.id ?: ""
        val data = hashMapOf(
            "name" to name,
            "duration" to duration,
            "price" to price,
            "shopId" to currentShopId
        )
        if (serviceToEdit.value == null) db.collection("services").add(data)
        else db.collection("services").document(serviceToEdit.value!!.id).set(data)
        showAddServiceDialog.value = false
    }

    fun saveShop(name: String, address: String, phone: String, priceRange: String, rating: Double, imageUrl: String) {
        val data = hashMapOf(
            "name" to name,
            "address" to address,
            "phone" to phone,
            "priceRange" to priceRange,
            "rating" to rating,
            "imageUrl" to imageUrl
        )
        if (shopToEdit.value == null) db.collection("shops").add(data)
        else db.collection("shops").document(shopToEdit.value!!.id).set(data)
        showAddShopDialog.value = false
    }

    private fun updateStatistics() {
        // Filter bookings by time range
        val now = System.currentTimeMillis()
        val timeRangeMs = when (_statsTimeRange.value) {
            "Hôm nay" -> 24 * 60 * 60 * 1000L
            "Tuần" -> 7 * 24 * 60 * 60 * 1000L
            "Tháng" -> 30 * 24 * 60 * 60 * 1000L
            "Năm" -> 365 * 24 * 60 * 60 * 1000L
            else -> Long.MAX_VALUE
        }
        
        // For now, we'll use all bookings (can enhance by parsing dateTime to filter)
        val filteredBookings = bookings
        
        // recompute counts and revenue based on filtered bookings
        _totalBookingsCount.value = filteredBookings.size
        val serviceCount = mutableMapOf<String, Int>()
        val staffCount = mutableMapOf<String, Int>()
        var revenue = 0L
        
        for (b in filteredBookings) {
            val svcKey = b.service
            serviceCount[svcKey] = (serviceCount[svcKey] ?: 0) + 1
            staffCount[b.barber] = (staffCount[b.barber] ?: 0) + 1
            
            val priceStr = b.price
            val parsed = priceStr.replace(".", "").replace(",", "").filter { it.isDigit() }.toLongOrNull()
            if (parsed != null && parsed > 0L) {
                revenue += parsed
            } else {
                val s = services.find { it.name == b.service }
                if (s != null) revenue += s.price.toLong()
            }
        }
        
        _totalRevenue.value = revenue
        _popularServices.value = serviceCount.toList().sortedByDescending { it.second }.take(5)
        _staffPerformance.value = staffCount.toList().sortedByDescending { it.second }
        
        // Calculate additional metrics
        _avgRevenuePerBooking.value = if (filteredBookings.size > 0) revenue / filteredBookings.size else 0L
        _avgBookingsPerStaff.value = if (staffCount.isNotEmpty()) filteredBookings.size / staffCount.size else 0
        _completionRate.value = if (filteredBookings.size > 0) ((filteredBookings.size - 0) * 100) / filteredBookings.size else 0
    }

    fun logout() {
        try {
            FirebaseAuth.getInstance().signOut()
            _currentAdmin.value = null
        } catch (_: Exception) {}
    }
}