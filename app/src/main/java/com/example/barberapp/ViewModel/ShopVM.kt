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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopVM : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _allShops = MutableStateFlow(listOf<Shop?>(null))
    private val _shop =  MutableStateFlow<Shop?>(null)
    val shop : StateFlow<Shop?> =_shop
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews
    private val shopRepo = ShopRepository()
    init {
        fetchAllShops()
    }
    @OptIn(FlowPreview::class)
    val filteredShops = searchText.debounce(300L).combine(_allShops) { text, shops ->
        if (text.isBlank()) {
            shops
        }else{
            shops.filter { (it?.name?.contains(text, ignoreCase = true) ?: "Loading...") == true }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = _allShops.value
    )
    fun onSearchTextChange(newText: String){
        _searchText.value = newText
    }

    fun fetchAllShops(){
        viewModelScope.launch {
            val shopsFromDB = shopRepo.getAllShopData()
            _allShops.value = shopsFromDB
        }
    }
    fun loadShopDetails(shopId: String){
        shopRepo.getShopDetailData(shopId){shopInfo ->
            _shop.value = shopInfo
        }
        shopRepo.getReviewsForShop(shopId){reviewList ->
            _reviews.value = reviewList
        }
    }
}