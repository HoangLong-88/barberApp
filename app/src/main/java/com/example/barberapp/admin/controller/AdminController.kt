package com.example.barberapp.admin.controller

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.barberapp.admin.model.BookingItem
import com.example.barberapp.admin.model.ServiceItem
import com.example.barberapp.admin.model.ShopItem
import com.example.barberapp.admin.model.UserItem
import com.google.firebase.firestore.FirebaseFirestore

class AdminController {
    private val db = FirebaseFirestore.getInstance()

    // --- State (Dữ liệu mà View sẽ quan sát) ---
    var currentTab = mutableStateOf("Tiệm")
    val users = mutableStateListOf<UserItem>()
    val services = mutableStateListOf<ServiceItem>()
    val bookings = mutableStateListOf<BookingItem>()
    val shops = mutableStateListOf<ShopItem>()
    
    // Tìm kiếm
    var searchQuery = mutableStateOf("")
    
    // Tiệm được chọn để xem/thêm dịch vụ
    var selectedShopForService = mutableStateOf<ShopItem?>(null)
    
    var selectedUserFilter = mutableStateOf("Tất cả")
    var selectedDateFilter = mutableStateOf("Tất cả")

    // Dialog States
    var showAddUserDialog = mutableStateOf(false)
    var userToEdit = mutableStateOf<UserItem?>(null)
    var showAddServiceDialog = mutableStateOf(false)
    var serviceToEdit = mutableStateOf<ServiceItem?>(null)
    var showAddShopDialog = mutableStateOf(false)
    var shopToEdit = mutableStateOf<ShopItem?>(null)
    var itemToDelete = mutableStateOf<Any?>(null)

    // --- Logic xử lý (Controller) ---
    
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
                if (selectedShopForService.value == null && fetchedShops.isNotEmpty()) {
                    selectedShopForService.value = fetchedShops.first()
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

    fun deleteItem(item: Any) {
        when (item) {
            is UserItem -> db.collection("users").document(item.id).delete()
            is ServiceItem -> db.collection("services").document(item.id).delete()
            is ShopItem -> db.collection("shops").document(item.id).delete()
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
        val currentShopId = selectedShopForService.value?.id ?: ""
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
