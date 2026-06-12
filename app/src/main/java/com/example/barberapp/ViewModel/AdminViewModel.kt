package com.example.barberapp.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.barberapp.Model.entities.BookingItem
import com.example.barberapp.Model.entities.ServiceItem
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.UserItem
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // --- State ---
    private val _currentTab = mutableStateOf("Tiệm")
    val currentTab: State<String> = _currentTab

    val users = mutableStateListOf<UserItem>()
    val services = mutableStateListOf<ServiceItem>()
    val bookings = mutableStateListOf<BookingItem>()
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

    // Dialog States
    val showAddUserDialog = mutableStateOf(false)
    val userToEdit = mutableStateOf<UserItem?>(null)
    val showAddServiceDialog = mutableStateOf(false)
    val serviceToEdit = mutableStateOf<ServiceItem?>(null)
    val showAddShopDialog = mutableStateOf(false)
    val shopToEdit = mutableStateOf<Shop?>(null)
    val itemToDelete = mutableStateOf<Any?>(null)

    init {
        fetchData()
    }

    private fun fetchData() {
        // Listen Users
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
    }

    // --- Actions ---
    fun setCurrentTab(tab: String) { _currentTab.value = tab }
    fun setSearchQuery(q: String) { _searchQuery.value = q }
    fun setSelectedUserFilter(f: String) { _selectedUserFilter.value = f }
    fun setSelectedDateFilter(f: String) { _selectedDateFilter.value = f }
    fun setSelectedShopForService(shop: Shop?) { _selectedShopForService.value = shop }

    fun deleteItem(item: Any) {
        when (item) {
            is UserItem -> db.collection("users").document(item.id).delete()
            is ServiceItem -> db.collection("services").document(item.id).delete()
            is Shop -> db.collection("shops").document(item.id).delete()
            is BookingItem -> db.collection("bookings").document(item.id).delete()
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

    fun saveService(name: String, duration: String, price: String) {
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

    fun logout() {
        // TODO: Implement real logout with Firebase Auth
        _currentAdmin.value = null
    }
}