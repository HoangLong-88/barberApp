package com.example.barberapp.View.screenUI.customer.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barberapp.View.component.BarberShopCard
import com.example.barberapp.View.component.EmptyStateLabel
import com.example.barberapp.View.utils.BackgroundColor
import com.example.barberapp.View.utils.OnSurface
import com.example.barberapp.ViewModel.ShopVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    shopVM: ShopVM,
) {
    val favoriteShops by shopVM.favoriteShops.collectAsState()
    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorites",
                        color = OnSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor
                )
            )
        }
    ) { innerPadding ->
        if (favoriteShops.isEmpty()) {
            EmptyStateLabel(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                imageVector = Icons.Default.FavoriteBorder,
                primaryMessage = "No favorites yet",
                hintMessage = "Tap the heart icon on any shop to save it"
            )
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favoriteShops) { shop ->
                    BarberShopCard(
                        shop = shop,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        onClick = { navController.navigate("shop_details/${shop?.id?:""}") },
                        onFavoriteClick = { shopId, isFav ->
                            shopVM.toggleFavoriteShop(shopId, isFav)
                        }
                    )
                }
            }
        }
    }
}
