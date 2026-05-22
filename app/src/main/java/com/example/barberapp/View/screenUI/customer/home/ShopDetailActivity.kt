package com.example.barberapp.View.screenUI.customer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.Model.entities.Review
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.View.component.BarberCard
import com.example.barberapp.ViewModel.ShopVM

// ─── Color Palette ───────────────────────────────────────────────────────────

private val BackgroundDark = Color(0xFF111111)
private val SurfaceDark = Color(0xFF1E1E1E)
val CardDark = Color(0xFF252525)
private val GoldPrimary = Color(0xFFF5A623)
private val GoldLight = Color(0xFFFFC85A)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFAAAAAA)
private val TabInactive = Color(0xFF888888)
private val DividerColor = Color(0xFF2E2E2E)
val AvatarBg = Color(0xFF2E2E2E)

// ─── Data Models ─────────────────────────────────────────────────────────────

data class Barber(
    val id: Int,
    val name: String,
    val rating: Float,
)


// ─── Tabs ────────────────────────────────────────────────────────────────────

private enum class ShopTab(val label: String) {
    SERVICES("Services"),
    BARBERS("Barbers"),
    REVIEWS("Reviews")
}

// ─── Root Screen ─────────────────────────────────────────────────────────────

@Composable
fun ShopDetailScreen(
    shopId: String,
    shopVM: ShopVM = viewModel(),
    onBook: (Service?) -> Unit = {},
    navController: NavController
) {
    var isFavourite by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(ShopTab.SERVICES) }
    val shopState by shopVM.shop.collectAsState()
    val reviewState by shopVM.reviews.collectAsState()
    LaunchedEffect(shopId) {
        shopVM.loadShopDetails(shopId)
    }
    val shop = shopState ?: return Box(Modifier
        .fillMaxSize()
        .background(BackgroundDark))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // ── Hero Image ────────────────────────────────────────────────
            item {
                HeroSection(
                    isFavourite,
                    onBack = { navController.popBackStack() },
                    onFavClick = { isFavourite = !isFavourite })
            }

            // ── Shop Meta ─────────────────────────────────────────────────
            item { ShopMetaSection(shop) }

            // ── Tab Row ───────────────────────────────────────────────────
            item {
                ShopTabRow(
                    selected = selectedTab,
                    onSelect = { tab ->
                        selectedTab = tab
                        if (tab == ShopTab.REVIEWS) {
                            shopVM.loadShopDetails(shopId)
                        }
                    }
                )
            }

            // ── Tab Content ───────────────────────────────────────────────
            when (selectedTab) {
                ShopTab.SERVICES -> {
                    items(shop.services, key = { it.id }) { service ->
                        ServiceCard(service = service, onBook = { onBook(service) })
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }

                ShopTab.REVIEWS -> {
                    items(reviewState, key = { it.id }) { review ->
                        ReviewCard(review)
                    }
                    item { WriteReviewButton({ navController.navigate("reviews/$shopId") }) }
                    item { Spacer(Modifier.height(24.dp)) }
                }

                ShopTab.BARBERS -> {
                    items(shop.barbers, key = { it.id }) { barber ->
                        BarberCard(barber = barber)
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}

// ─── Hero Section ────────────────────────────────────────────────────────────

@Composable
private fun HeroSection(
    isFavourite: Boolean,
    onBack: () -> Unit,
    onFavClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        // Dark gradient placeholder (replace Box with AsyncImage / Coil in production)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF2A1A0A), Color(0xFF0D0D0D))
                    )
                )
        )

        // Scrim so buttons stay readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x80000000), Color.Transparent)
                    )
                )
        )

        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .size(40.dp)
                .background(Color(0x66000000), RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary
            )
        }

        // Favourite button
        IconButton(
            onClick = onFavClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(40.dp)
                .background(Color(0x66000000), RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favourite",
                tint = if (isFavourite) GoldPrimary else TextPrimary
            )
        }
    }
}

// ─── Shop Meta ───────────────────────────────────────────────────────────────

@Composable
private fun ShopMetaSection(shop: Shop) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = shop.name,
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = shop.rating.toString(),
                color = GoldPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(Modifier.width(12.dp))
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = shop.address,
                color = TextSecondary,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Phone,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(text = shop.phone, color = TextSecondary, fontSize = 13.sp)
        }
    }

    Divider(color = DividerColor, thickness = 1.dp)
}

// ─── Tab Row ─────────────────────────────────────────────────────────────────

@Composable
private fun ShopTabRow(
    selected: ShopTab,
    onSelect: (ShopTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        ShopTab.entries.forEach { tab ->
            val isActive = tab == selected
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                TextButton(onClick = { onSelect(tab) }) {
                    Text(
                        text = tab.label,
                        color = if (isActive) GoldPrimary else TabInactive,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth(0.6f)
                            .background(GoldPrimary, RoundedCornerShape(1.dp))
                    )
                }
            }
        }
    }

    Divider(color = DividerColor, thickness = 1.dp)
}

// ─── Service Card ─────────────────────────────────────────────────────────────

@Composable
private fun ServiceCard(
    service: Service?,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service?.name ?: "Loading...",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Price: %,d VND".format(service?.price ?: "Loading..."),
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onBook,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Book",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// ─── Review Card ──────────────────────────────────────────────────────────────

@Composable
private fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.userName,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                StarRow(rating = review.rating)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = review.comment,
                color = TextSecondary,
                fontSize = 13.sp
            )
        }
    }
}

// ─── Star Row ────────────────────────────────────────────────────────────────

@Composable
private fun StarRow(rating: Float, maxStars: Int = 5) {
    Row {
        repeat(maxStars) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

// ─── Write Review Button ─────────────────────────────────────────────────────

@Composable
private fun WriteReviewButton(onClick: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary),
            border = ButtonDefaults.outlinedButtonBorder.copy()
        ) {
            Text(
                text = "Write a Review",
                color = GoldPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF111111)
//@Composable
//fun ShopDetailScreenPreview() {
//    ShopDetailScreen(navController = rememberNavController())
//}