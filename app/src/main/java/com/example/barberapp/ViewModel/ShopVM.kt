package com.example.barberapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.Model.entities.Review
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Repository.ShopRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopVM : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _allShops = MutableStateFlow<List<Shop?>>(emptyList())
    private val _shop = MutableStateFlow<Shop?>(null)
    val shop: StateFlow<Shop?> = _shop
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews
    private val shopRepo = ShopRepository()
    val favoriteShops: StateFlow<List<Shop?>> = _allShops
        .map { shops -> shops.filter { it?.isFavorite == true} }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private var currentUserId: String = ""   // ← thêm

    // Gọi hàm này 1 lần từ AppNavHost sau khi userVM.userData sẵn sàng
    fun init(userId: String) {
        if (currentUserId == userId) return  // tránh fetch lại nếu đã init
        currentUserId = userId
        fetchAllShops()
    }


    @OptIn(FlowPreview::class)
    val filteredShops = searchText.debounce(300L).combine(_allShops) { text, shops ->
        if (text.isBlank()) {
            shops
        } else {
            shops.filter { (it?.name?.contains(text, ignoreCase = true) ?: "Loading...") == true }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = _allShops.value
    )

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    fun fetchAllShops() {
        viewModelScope.launch {
            shopRepo.getAllShopData(currentUserId) { allShop ->
                _allShops.value = allShop
            }
        }
    }

    fun loadShopDetails(shopId: String) {
        shopRepo.getShopDetailData(shopId) { shopInfo ->
            _shop.value = shopInfo
        }
        shopRepo.getReviewsForShop(shopId) { reviewList ->
            _reviews.value = reviewList
        }
    }

    fun loadReviewOnly(shopId: String) {
        shopRepo.getReviewsForShop(shopId) { reviewList ->
            _reviews.value = reviewList
        }
    }
    fun toggleFavoriteShop(shopId: String, isFav: Boolean) {
        // 1. Thực hiện update lên Firestore Database tại đây (nếu có)
         shopRepo.updateFavoriteStatus(currentUserId,shopId, isFav){success ->
             if (!success) {
                 println("Ko thêm favorites đc")
             }}

        // 2. Cập nhật lại danh sách _allShops ngay tại local để UI thay đổi lập tức
        _allShops.value = _allShops.value.map { shop ->
            if (shop?.id == shopId) {
                shop.copy(isFavorite = isFav)
            } else {
                shop
            }
        }
        if (_shop.value?.id ==shopId){
            _shop.value = _shop.value?.copy(isFavorite = isFav)
        }
    }
}