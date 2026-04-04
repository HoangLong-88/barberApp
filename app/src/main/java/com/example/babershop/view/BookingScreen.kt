package com.example.babershop.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
// IMPORT MỚI
import com.example.babershop.model.BookingItem
import com.example.babershop.controller.BookingController

@Composable
fun BookingsScreen(controller: BookingController = remember { BookingController() }) {
    val filteredBookings = controller.getFilteredBookings()

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0D0D0D)).padding(16.dp)
    ) {
        Text("My Bookings", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Text("Your booking history", color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All","Completed","Pending","Cancelled").forEach { filter ->
                FilterChip(
                    selected = controller.selectedFilter == filter,
                    onClick = { controller.selectedFilter = filter },
                    label = { Text(text = filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFFC107),
                        selectedLabelColor = Color.Black,
                        containerColor = Color(0xFF1A1A1A),
                        labelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn {
            items(filteredBookings) { booking ->
                BookingCard(booking)
            }
        }
    }
}

@Composable
fun BookingCard(booking: BookingItem) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(booking.service, color = Color.White)
                Text(booking.shop, color = Color.Gray)
                Text(booking.time, color = Color.Gray)
                Text("Barber: ${booking.barber}", color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                StatusChip(booking.status)
                Spacer(modifier = Modifier.height(8.dp))
                Text(booking.price, color = Color(0xFFFFC107))
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when(status){
        "Completed" -> Color(0xFF2E7D32)
        "Pending" -> Color(0xFFFFC107)
        "Cancelled" -> Color(0xFFD32F2F)
        else -> Color.Gray
    }
    Surface(color = color, shape = MaterialTheme.shapes.small) {
        Text(status, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), color = Color.White)
    }
}