package com.example.barberapp.View.screenUI.employee

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barberapp.ViewModel.AuthVM
import com.example.barberapp.ViewModel.UserVM
import com.example.barberapp.Model.entities.EmployeeBookingItem
import com.example.barberapp.ViewModel.EmpViewModel

@Composable
fun EmployeeScreen(
    viewModel: EmpViewModel = viewModel(),
    navController: NavController,
    authVM: AuthVM,
    userVM: UserVM
) {
    val currentTab by viewModel.currentTab
    val viewMode by viewModel.viewMode
    val EmployeeInfo by viewModel.employeeInfo
    val filteredBookings by viewModel.filteredBookings
    
    var selectedBookingForDetail by remember { mutableStateOf<EmployeeBookingItem?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            EmployeeHeader(EmployeeInfo, navController, authVM, userVM)
            EmployeeStats(EmployeeInfo)
            
            Spacer(modifier = Modifier.height(24.dp))

            // Main Tabs (Lịch, Đánh giá, Hồ sơ)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                EmployeeTabButton("Lịch", Icons.Default.DateRange, currentTab == "Lịch", Modifier.weight(1f)) { viewModel.setTab("Lịch") }
                EmployeeTabButton("Đánh giá", Icons.Default.ThumbUp, currentTab == "Đánh giá", Modifier.weight(1f)) { viewModel.setTab("Đánh giá") }
                EmployeeTabButton("Hồ sơ", Icons.Default.Person, currentTab == "Hồ sơ", Modifier.weight(1f)) { viewModel.setTab("Hồ sơ") }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (currentTab == "Lịch") {
                CalendarSection(viewModel)
                
                Spacer(modifier = Modifier.height(20.dp))

                // View Mode Toggle (Timeline / Danh sách)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("${filteredBookings.size} lịch hẹn", color = Color.White, fontWeight = FontWeight.Bold)
                    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(20.dp)) {
                        Row {
                            ViewModeButton("Timeline", viewMode == "Timeline") { viewModel.setViewMode("Timeline") }
                            ViewModeButton("Danh sách", viewMode == "Danh sách") { viewModel.setViewMode("Danh sách") }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Booking Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = if (viewMode == "Timeline") Arrangement.Top else Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    if (viewMode == "Timeline") {
                        val hours = (9..21).map { "${it.toString().padStart(2, '0')}:00" }
                        items(hours) { hour ->
                            val booking = filteredBookings.find { it.time == hour }
                            TimelineRow(
                                time = hour,
                                booking = booking,
                                onConfirm = { booking?.let { viewModel.confirmBooking(it.id) } },
                                onCancel = { booking?.let { viewModel.cancelBooking(it.id) } },
                                onBookingClick = { selectedBookingForDetail = it }
                            )
                        }
                    } else {
                        if (filteredBookings.isEmpty()) {
                            item {
                                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Không có lịch hẹn nào", color = Color.Gray)
                                }
                            }
                        } else {
                            items(filteredBookings) { booking ->
                                BookingCardList(
                                    booking,
                                    onConfirm = { viewModel.confirmBooking(booking.id) },
                                    onCancel = { viewModel.cancelBooking(booking.id) },
                                    onClick = { selectedBookingForDetail = booking }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Bottom Sheet chi tiết
    selectedBookingForDetail?.let { booking ->
        BookingDetailSheet(
            booking = booking,
            onDismiss = { selectedBookingForDetail = null }
        )
    }
}

@Composable
fun CalendarSection(viewModel: EmpViewModel) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(viewModel.dateList) { dateItem ->
            Surface(
                modifier = Modifier.width(55.dp).height(70.dp).clickable { viewModel.selectDate(dateItem) },
                color = if (dateItem.isSelected) Color(0xFFEBC14F) else Color(0xFF1E1E1E),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(dateItem.dayOfWeek, color = if (dateItem.isSelected) Color.Black else Color.Gray, fontSize = 12.sp)
                    Text(dateItem.dayOfMonth, color = if (dateItem.isSelected) Color.Black else Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EmployeeTabButton(label: String, icon: ImageVector, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        color = if (isSelected) Color(0xFFEBC14F) else Color(0xFF1E1E1E),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(vertical = 12.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isSelected) Color.Black else Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, color = if (isSelected) Color.Black else Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ViewModeButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = if (isSelected) Color(0xFFEBC14F) else Color.Transparent,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(label, color = if (isSelected) Color.Black else Color.Gray, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
