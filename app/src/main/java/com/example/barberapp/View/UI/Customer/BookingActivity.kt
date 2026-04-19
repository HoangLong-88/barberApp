package com.example.barberapp.View.UI.Customer

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.UI.component.SharedBottomNavBar
import com.example.barberapp.View.utils.BackgroundColor
import com.example.barberapp.View.utils.CancelledBg
import com.example.barberapp.View.utils.CancelledText
import com.example.barberapp.View.utils.CompletedBg
import com.example.barberapp.View.utils.CompletedText
import com.example.barberapp.View.utils.FilterInactive
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.PendingBg
import com.example.barberapp.View.utils.PendingText
import com.example.barberapp.View.utils.PrimaryYellow
import com.example.barberapp.View.utils.SurfaceColor
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary

// ─── Data Models ─────────────────────────────────────────────────────────────
enum class BookingStatus { Completed, Pending, Cancelled }

enum class FilterTab { All, Completed, Pending, Cancelled }

data class Booking(
    val id: Int,
    val service: String,
    val shop: String,
    val dateTime: String,
    val barber: String,
    val price: String,
    val status: BookingStatus
)

// ─── Sample Data ─────────────────────────────────────────────────────────────

private val sampleBookings = listOf(
    Booking(1, "Hair Cut",     "King Barber Shop",   "20 May – 17:00", "John",  "80.000 VND",  BookingStatus.Completed),
    Booking(2, "Beard Shave",  "King Barber Shop",   "18 May – 14:00", "Mike",  "40.000 VND",  BookingStatus.Completed),
    Booking(3, "Hair Styling", "Elite Cuts Studio",  "25 May – 10:00", "David", "150.000 VND", BookingStatus.Pending),
    Booking(4, "Hair Cut",     "Classic Barber",     "28 May – 09:00", "John",  "70.000 VND",  BookingStatus.Cancelled),
)

// ─── Main Screen ─────────────────────────────────────────────────────────────

@Composable
fun MyBookingsScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf(FilterTab.All) }

    val filteredBookings = remember(selectedFilter) {
        when (selectedFilter) {
            FilterTab.All       -> sampleBookings
            FilterTab.Completed -> sampleBookings.filter { it.status == BookingStatus.Completed }
            FilterTab.Pending   -> sampleBookings.filter { it.status == BookingStatus.Pending }
            FilterTab.Cancelled -> sampleBookings.filter { it.status == BookingStatus.Cancelled }
        }
    }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = { SharedBottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))

            // Header
            Text(
                text = "My Bookings",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your booking history",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(Modifier.height(20.dp))

            // Filter Tabs
            FilterTabRow(
                selected = selectedFilter,
                onSelect = { selectedFilter = it }
            )

            Spacer(Modifier.height(16.dp))

            // Bookings List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredBookings, key = { it.id }) { booking ->
                    BookingCard(booking = booking)
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}

// ─── Filter Tab Row ───────────────────────────────────────────────────────────

@Composable
fun FilterTabRow(
    selected: FilterTab,
    onSelect: (FilterTab) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        FilterTab.entries.forEach { tab ->
            FilterChip(
                label = tab.name,
                isSelected = selected == tab,
                onClick = { onSelect(tab) }
            )
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryYellow else FilterInactive,
        animationSpec = tween(200),
        label = "chipBg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.Black else TextSecondary,
        animationSpec = tween(200),
        label = "chipText"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 9.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ─── Booking Card ─────────────────────────────────────────────────────────────

@Composable
fun BookingCard(booking: Booking) {
    val (statusBg, statusFg) = when (booking.status) {
        BookingStatus.Completed -> CompletedBg to CompletedText
        BookingStatus.Pending   -> PendingBg to PendingText
        BookingStatus.Cancelled -> CancelledBg to CancelledText
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = booking.service,
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(
                    label = booking.status.name,
                    background = statusBg,
                    textColor = statusFg
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(booking.shop,     color = TextSecondary, fontSize = 13.sp)
            Text(booking.dateTime, color = TextSecondary, fontSize = 13.sp)
            Text("Barber: ${booking.barber}", color = TextSecondary, fontSize = 13.sp)

            Spacer(Modifier.height(10.dp))

            Text(
                text = booking.price,
                color = statusFg,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

// ─── Status Badge ─────────────────────────────────────────────────────────────

@Composable
fun StatusBadge(label: String, background: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ─── Bottom Navigation Bar ────────────────────────────────────────────────────

private data class NavItem(val label: String, val icon: ImageVector)

private val navItems = listOf(
    NavItem("Home",          Icons.Outlined.Home),
    NavItem("Bookings",      Icons.Outlined.CalendarMonth),
    NavItem("Notifications", Icons.Outlined.Notifications),
    NavItem("Profile",       Icons.Outlined.Person),
)

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF121212, showSystemUi = true)
@Composable
fun MyBookingsScreenPreview() {
    MyBookingsScreen(navController = rememberNavController())
}