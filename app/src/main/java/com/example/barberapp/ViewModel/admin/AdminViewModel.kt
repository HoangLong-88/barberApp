package com.example.barberapp.ViewModel.admin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.barberapp.Model.entities.BookingItem
import com.example.barberapp.Model.entities.ServiceItem
import com.example.barberapp.Model.entities.ShopItem
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
    val shops = mutableStateListOf<ShopItem>()

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedShopForService = mutableStateOf<ShopItem?>(null)
    val selectedShopForService: State<ShopItem?> = _selectedShopForService

    private val _selectedUserFilter = mutableStateOf("Tất cả")
    val selectedUserFilter: State<String> = _selectedUserFilter

    private val _selectedDateFilter = mutableStateOf("Tất cả")
    val selectedDateFilter: State<String> = _selectedDateFilter

    // Dialog States
    val showAddUserDialog = mutableStateOf(false)
    val userToEdit = mutableStateOf<UserItem?>(null)
    val showAddServiceDialog = mutableStateOf(false)
    val serviceToEdit = mutableStateOf<ServiceItem?>(null)
    val showAddShopDialog = mutableStateOf(false)
    val shopToEdit = mutableStateOf<ShopItem?>(null)
    val itemToDelete = mutableStateOf<Any?>(null)

    init {
        fetchData()
    }

    private fun fetchData() {
        // Listen Users
        db.collection("users").addSnapshotListener { v, _ ->
            v?.let {
                users.clear()
                users.addAll(it.documents.mapNotNull { d -> d.toObject(UserItem::class.java)?.copy(id = d.id) })
            }
        }
        // Listen Services
        db.collection("services").addSnapshotListener { v, _ ->
            v?.let {
                services.clear()
                services.addAll(it.documents.mapNotNull { d -> d.toObject(ServiceItem::class.java)?.copy(id = d.id) })
            }
        }
        // Listen Shops
        db.collection("shops").addSnapshotListener { v, _ ->
            v?.let {
                shops.clear()
                val fetchedShops = it.documents.mapNotNull { d -> d.toObject(ShopItem::class.java)?.copy(id = d.id) }
                shops.addAll(fetchedShops)
                if (_selectedShopForService.value == null && fetchedShops.isNotEmpty()) {
                    _selectedShopForService.value = fetchedShops.first()
                }
            }
        }
        // Giả lập dữ liệu booking
        bookings.clear()
        bookings.addAll(listOf(
            BookingItem("1", "Nguyen Van A", "Hair Cut", "John", "Mon 17/03 - 09:00", "Completed"),
            BookingItem("2", "Tran Van B", "Beard Shave", "Mike", "Mon 17/03 - 10:00", "Pending")
        ))
    }

    // --- Actions ---
    fun setCurrentTab(tab: String) { _currentTab.value = tab }
    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSelectedShopForService(shop: ShopItem) { _selectedShopForService.value = shop }
    fun setSelectedUserFilter(filter: String) { _selectedUserFilter.value = filter }
    fun setSelectedDateFilter(filter: String) { _selectedDateFilter.value = filter }

    fun deleteItem(item: Any) {
        when (item) {
            is UserItem -> db.collection("users").document(item.id).delete()
            is ShopItem -> db.collection("shops").document(item.id).delete()
            is ServiceItem -> db.collection("services").document(item.id).delete()
        }
        itemToDelete.value = null
    }

    fun saveUser(name: String, email: String, phone: String, role: String) {
        val colorHex = when(role) { "employee" -> "#4CAF50"; "manager" -> "#9C27B0"; else -> "#2196F3" }
        val data = hashMapOf("name" to name, "email" to email, "phone" to phone, "role" to role, "roleColorHex" to colorHex)
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

    fun saveShop(name: String, address: String, phone: String, priceRange: String, rating: String, imageUrl: String) {
        val data = hashMapOf("name" to name, "address" to address, "phone" to phone, "priceRange" to priceRange, "rating" to rating, "imageUrl" to imageUrl)
        if (shopToEdit.value == null) db.collection("shops").add(data)
        else db.collection("shops").document(shopToEdit.value!!.id).set(data)
        showAddShopDialog.value = false
    }
}