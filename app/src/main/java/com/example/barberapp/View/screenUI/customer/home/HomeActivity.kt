package com.example.barberapp.View.screenUI.customer.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.component.BarberShopCard
import com.example.barberapp.View.component.PromoBanner
import com.example.barberapp.View.component.SearchBar
import com.example.barberapp.View.component.SharedBottomNavBar
import com.example.barberapp.View.layout.NearbyHeader
import com.example.barberapp.View.layout.TopHeader
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.ViewModel.ShopVM
import com.example.barberapp.ViewModel.UserVM

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun HomeScreen(modifier: Modifier = Modifier,
               navController: NavController,
               shopVM: ShopVM,
               userVM: UserVM,
) {
    val searchText by shopVM.searchText.collectAsState()
    val shops by shopVM.filteredShops.collectAsState()
    val focusManager = LocalFocusManager.current
    val isReady by shopVM.isReady.collectAsState()
    val userInfo = userVM.userData

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = { SharedBottomNavBar(navController) }
    ) { innerPadding ->
        if (!isReady) {
            // Hiện spinner khi đang fetch
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Header
                item { TopHeader(avatarUrl = userInfo?.avatarUrl) }

                // Search bar
                item {
                    SearchBar(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        query = searchText,
                        onQueryChange = shopVM::onSearchTextChange,
                        onSearch = {
                            println("Đang tìm kiếm Barber shop với từ khóa: $searchText")
                        }
                    )
                }

                // Promo banner
                item {
                    PromoBanner(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Section title
                item {
                    NearbyHeader(
                        modifier = Modifier.padding(
                            start = 16.dp, end = 16.dp,
                            top = 16.dp, bottom = 8.dp
                        )
                    )
                }

                // Barber shop cards
                items(shops) { shop ->
                    BarberShopCard(
                        shop = shop,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        onClick = { navController.navigate("shop_details/${shop?.id}") },
                        onFavoriteClick = { shopId, isFav ->
                            shopVM.toggleFavoriteShop(
                                shopId,
                                isFav
                            )
                        }
                    )
                }
            }
        }
    }
}
