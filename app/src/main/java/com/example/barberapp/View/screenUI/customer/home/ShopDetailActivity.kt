package com.example.barberapp.View.screenUI.customer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.types.ShopDetailTab
import com.example.barberapp.View.component.BarberDetailsCardInCustomer
import com.example.barberapp.View.component.ReviewCardInCustomer
import com.example.barberapp.View.component.ServiceDetailsCardInCustomer
import com.example.barberapp.View.component.WriteReviewButton
import com.example.barberapp.View.layout.HeroSection
import com.example.barberapp.View.layout.ShopDetailTabRow
import com.example.barberapp.View.layout.ShopMetaSection
import com.example.barberapp.ViewModel.ShopVM

// ─── Color Palette ───────────────────────────────────────────────────────────

private val BackgroundDark = Color(0xFF111111)
private val SurfaceDark = Color(0xFF1E1E1E)
val CardDark = Color(0xFF252525)
val GoldPrimary = Color(0xFFF5A623)
private val GoldLight = Color(0xFFFFC85A)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFAAAAAA)
val TabInactive = Color(0xFF888888)
val DividerColor = Color(0xFF2E2E2E)
val AvatarBg = Color(0xFF2E2E2E)

@Composable
fun ShopDetailScreen(
    shopId: String,
    shopVM: ShopVM,
    onBook: (Service?) -> Unit = {},
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(ShopDetailTab.SERVICES) }
    val shopState by shopVM.shop.collectAsState()
    val reviewState by shopVM.reviews.collectAsState()
    var selectedServices by remember { mutableStateOf(setOf<Service>()) }

    LaunchedEffect(shopId) {
        shopVM.loadShopDetails(shopId)
    }
    val shop = shopState ?: return Box(
        Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // ── Hero Image ────────────────────────────────────────────────
            item {
                HeroSection(
                    isFavourite = shop.isFavorite,
                    onBack = { navController.popBackStack() },
                    onFavClick = { shopVM.toggleFavoriteShop(shop.id,!shop.isFavorite) })
            }

            // ── Shop Meta ─────────────────────────────────────────────────
            item { ShopMetaSection(shop) }

            // ── Tab Row ───────────────────────────────────────────────────
            item {
                ShopDetailTabRow(
                    selected = selectedTab,
                    onSelect = { tab ->
                        selectedTab = tab
                        if (tab == ShopDetailTab.REVIEWS) {
                            shopVM.loadReviewOnly(shopId)
                        }
                    }
                )
            }

            // ── Tab Content ───────────────────────────────────────────────
            when (selectedTab) {
                ShopDetailTab.SERVICES -> {
                    items(shop.services, key = { it.id }) { service ->
                        val isSelected = selectedServices.contains(service)
                        ServiceDetailsCardInCustomer(
                            service = service, isSelected = isSelected,
                            onBook = {
                                selectedServices = if (isSelected) {
                                    selectedServices - service
                                } else {
                                    selectedServices + service
                                }
                            })
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }

                ShopDetailTab.REVIEWS -> {
                    items(reviewState, key = { it.id }) { review ->
                        ReviewCardInCustomer(review)
                    }
                    item { WriteReviewButton({ navController.navigate("reviews/${shop.id}") }) }
                    item { Spacer(Modifier.height(24.dp)) }
                }

                ShopDetailTab.BARBERS -> {
                    items(shop.barbers, key = { it.id }) { barber ->
                        BarberDetailsCardInCustomer(barber = barber)
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }

        if (selectedTab == ShopDetailTab.SERVICES && selectedServices.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    // Thêm hiệu ứng gradient đen mờ dần lên trên để UI nhìn sang trọng hơn
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, BackgroundDark)
                        )
                    )
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        val serviceIdsString = selectedServices.map { it.id }.joinToString(",")
                        val servicesToBook = selectedServices.toList()
                        onBook(servicesToBook.first()) // Hoặc update hàm onBook truyền List
                        navController.navigate("booking_checkout/${shop.id}/$serviceIdsString")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text(
                        text = "Confirm Booking (${selectedServices.size})",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


