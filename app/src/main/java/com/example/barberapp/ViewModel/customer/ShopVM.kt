package com.example.barberapp.ViewModel.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.Model.entities.Shop
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn

class ShopVM : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _allShops = MutableStateFlow(
        listOf(
            Shop(1, "King Barber Shop", "54 Nguyen Van Linh, Da Nang"),
            Shop(2, "Classic Cuts", "12 Tran Phu, Da Nang"),
            Shop(3, "Urban Fade", "88 Le Duan, Da Nang"),
        )
    )

    @OptIn(FlowPreview::class)
    val filteredShops = searchText.debounce(300L).combine(_allShops) { text, shops ->
        if (text.isBlank()) {
            shops
        }else{
            shops.filter { it.name.contains(text,ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _allShops.value
    )
    fun onSearchTextChange(newText: String){
        _searchText.value = newText
    }
}