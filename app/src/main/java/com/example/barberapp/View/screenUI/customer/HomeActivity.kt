package com.example.barberapp.View.screenUI.customer

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.component.BarberShopCard
import com.example.barberapp.View.component.PromoBanner
import com.example.barberapp.View.component.SearchBar
import com.example.barberapp.View.component.SharedBottomNavBar
import com.example.barberapp.View.layout.NearbyHeader
import com.example.barberapp.View.layout.TopHeader
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.GoldAccent

// ── Data models ───────────────────────────────────────────────────────────────
data class BarberShop(
    val id: Int,
    val name: String,
    val address: String,
    val priceRange: String,
    val rating: Double,
    val isFavorite: Boolean = false,
    // In a real app, use imageRes: Int or imageUrl: String
)

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    val shops = remember {
        listOf(
            BarberShop(1, "King Barber Shop", "54 Nguyen Van Linh, Da Nang", "80k – 150k", 4.8),
            BarberShop(2, "Classic Cuts",     "12 Tran Phu, Da Nang",        "50k – 100k", 4.5),
            BarberShop(3, "Urban Fade",       "88 Le Duan, Da Nang",         "70k – 130k", 4.7),
        )
    }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar      = { SharedBottomNavBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier            = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding      = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header
            item { TopHeader(userName = "JD") }

            // Search bar
            item {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(navController = rememberNavController())
    }
}