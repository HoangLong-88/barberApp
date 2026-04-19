package com.example.barberapp.View.UI.Customer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.UI.component.SharedBottomNavBar
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.BottomNavBg
import com.example.barberapp.View.utils.CardBg
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.LogoutRed
import com.example.barberapp.View.utils.SearchBg
import com.example.barberapp.View.utils.SurfaceDark
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary
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
                        top   = 16.dp, bottom = 8.dp
                    )
                )
            }

            // Barber shop cards
            items(shops) { shop ->
                BarberShopCard(
                    shop     = shop,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

// ── Top header ────────────────────────────────────────────────────────────────
@Composable
private fun TopHeader(userName: String) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = "Good evening",
                color      = TextSecondary,
                fontSize   = 13.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text       = "Find your barber ",
                    color      = TextPrimary,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "✂\uFE0F", fontSize = 18.sp)
            }
        }

        // Avatar circle
        Box(
            modifier         = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(SurfaceDark)
                .border(1.5.dp, GoldAccent.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = userName,
                color      = TextPrimary,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ── Search bar ────────────────────────────────────────────────────────────────
@Composable
private fun SearchBar(modifier: Modifier = Modifier) {
    Row(
        modifier          = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SearchBg)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = Icons.Outlined.Search,
            contentDescription = "Search",
            tint               = TextSecondary,
            modifier           = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text     = "Search barber shop...",
            color    = TextSecondary,
            fontSize = 14.sp
        )
    }
}

// ── Promo banner ──────────────────────────────────────────────────────────────
@Composable
private fun PromoBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFF1A1A1A), Color(0xFF2A2000))
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(GoldAccent.copy(alpha = 0.6f), Color.Transparent)
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        // Diagonal stripes decoration (right side)
        DiagonalStripes(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(30.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text       = "BARBER SHOP",
                color      = GoldAccent,
                fontSize   = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )

            Column {
                Text(
                    text       = "LIMITED OFFER",
                    color      = LogoutRed,
                    fontSize   = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Column {
                        Text(
                            text       = "Get 20% discount",
                            color      = TextPrimary,
                            fontSize   = 12.sp
                        )
                        Text(
                            text       = "for first haircut",
                            color      = TextSecondary,
                            fontSize   = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text       = "20%",
                        color      = TextPrimary.copy(alpha = 0.15f),
                        fontSize   = 52.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text       = "OFF",
                        color      = GoldAccent,
                        fontSize   = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Gift Now button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(GoldAccent)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text       = "GIFT NOW",
                    color      = BackgroundDark,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

// Simple diagonal stripes drawn with Canvas
@Composable
private fun DiagonalStripes(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stripeWidth = 6.dp.toPx()
        val gap         = 6.dp.toPx()
        val step        = stripeWidth + gap
        var x           = -size.height
        while (x < size.width + size.height) {
            drawRect(
                color    = GoldAccent.copy(alpha = 0.25f),
                topLeft  = androidx.compose.ui.geometry.Offset(x, 0f),
                size     = androidx.compose.ui.geometry.Size(stripeWidth, size.height * 2)
            )
            x += step
        }
    }
}

// ── Nearby header ─────────────────────────────────────────────────────────────
@Composable
private fun NearbyHeader(modifier: Modifier = Modifier) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text       = "Nearby Barbers",
            color      = TextPrimary,
            fontSize   = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text       = "Favorites",
            color      = GoldAccent,
            fontSize   = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Barber shop card ──────────────────────────────────────────────────────────
@Composable
private fun BarberShopCard(
    shop: BarberShop,
    modifier: Modifier = Modifier
) {
    var isFav by remember { mutableStateOf(shop.isFavorite) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
    ) {
        // Image placeholder with gradient overlay + heart button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF3A2800), Color(0xFF1A1200))
                    )
                )
        ) {
            // ── Replace Box below with AsyncImage / Image(painterResource(...)) ──
            Box(
                modifier         = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text      = "📸  Shop Image",
                    color     = TextSecondary.copy(alpha = 0.4f),
                    fontSize  = 14.sp
                )
            }

            // Gradient scrim at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, CardBg)
                        )
                    )
            )

            // Heart icon
            Box(
                modifier         = Modifier
                    .padding(12.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(BackgroundDark.copy(alpha = 0.5f))
                    .align(Alignment.TopEnd)
                    .clickable { isFav = !isFav },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = if (isFav) Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint               = if (isFav) LogoutRed else TextPrimary,
                    modifier           = Modifier.size(18.dp)
                )
            }
        }

        // Info section
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = shop.name,
                    color      = TextPrimary,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Filled.Star,
                        contentDescription = null,
                        tint               = GoldAccent,
                        modifier           = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text       = shop.rating.toString(),
                        color      = TextPrimary,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint               = TextSecondary,
                    modifier           = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text     = shop.address,
                    color    = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = shop.priceRange,
                    color    = TextSecondary,
                    fontSize = 13.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.clickable { }
                ) {
                    Text(
                        text       = "Book Now",
                        color      = GoldAccent,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "→", color = GoldAccent, fontSize = 13.sp)
                }
            }
        }
    }
}

// ── Bottom nav ────────────────────────────────────────────────────────────────
@Composable
private fun HomeBottomNavBar() {
    data class NavItem(val icon: ImageVector, val label: String, val selected: Boolean)

    val navItems = listOf(
        NavItem(Icons.Outlined.Home,          "Home",          true),
        NavItem(Icons.Outlined.DateRange,     "Bookings",      false),
        NavItem(Icons.Outlined.Notifications, "Notifications", false),
        NavItem(Icons.Outlined.Person,        "Profile",       false),
    )

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .background(BottomNavBg)
            .navigationBarsPadding()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        navItems.forEach { item ->
            val tint = if (item.selected) GoldAccent else TextSecondary
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier            = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector        = item.icon,
                    contentDescription = item.label,
                    tint               = tint,
                    modifier           = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text       = item.label,
                    color      = tint,
                    fontSize   = 11.sp,
                    fontWeight = if (item.selected) FontWeight.SemiBold else FontWeight.Normal
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