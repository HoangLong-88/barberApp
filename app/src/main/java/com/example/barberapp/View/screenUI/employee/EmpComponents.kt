package com.example.barberapp.View.screenUI.employee

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberapp.ViewModel.auth.AuthVM
import com.example.barberapp.ViewModel.customer.UserVM
import com.example.barberapp.Model.entities.EmployeeBookingItem
import com.example.barberapp.Model.entities.EmployeeInfo

@Composable
fun EmployeeHeader(
    info: EmployeeInfo,
    navController: NavController,
    authVM: AuthVM,
    userVM: UserVM
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFEBC14F)),
                contentAlignment = Alignment.Center
            ) {
                Text(info.name.first().toString(), color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("XIN CHÀO", color = Color.Gray, fontSize = 12.sp)
                Text(info.name + " 👋", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row {
            IconButton(onClick = {}) { Icon(Icons.Default.Notifications, null, tint = Color.White) }
            IconButton(onClick = {
                authVM.logOut(navController = navController, userVM = userVM)
            }) { Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = Color.White) }
        }
    }
}

@Composable
fun EmployeeStats(info: EmployeeInfo) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        StatCard("HÔM NAY", info.appointmentsToday.toString(), "lịch hẹn", Modifier.weight(1f))
        StatCard("HOÀN THÀNH", info.completedToday.toString(), "đã xong", Modifier.weight(1f))
        StatCard("RATING", info.rating.toString(), "${info.totalRatings} đánh giá", Modifier.weight(1f), true)
    }
}

@Composable
fun StatCard(label: String, value: String, subValue: String, modifier: Modifier, isRating: Boolean = false) {
    Surface(
        modifier = modifier,
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                if (isRating) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(16.dp).padding(start = 4.dp))
                }
            }
            Text(subValue, color = Color.Gray, fontSize = 10.sp)
        }
    }
}

@Composable
fun BookingCardList(booking: EmployeeBookingItem, onConfirm: () -> Unit, onCancel: () -> Unit, onClick: () -> Unit = {}) {
    Surface(
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF2C2C2C)), contentAlignment = Alignment.Center) {
                        Text(booking.customerName.first().toString().uppercase(), color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(booking.customerName, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("${booking.time} • ${booking.serviceName}", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(booking.price, color = Color(0xFFEBC14F), fontWeight = FontWeight.Bold)
                    StatusBadge(booking.status)
                }
            }
            if (booking.status == "Pending") {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f).height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F)),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("Xác nhận", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f).height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF351F1F)),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("Hủy", color = Color.Red, fontSize = 12.sp) }
                }
            }
        }
    }
}

@Composable
fun TimelineRow(time: String, booking: EmployeeBookingItem?, onConfirm: () -> Unit, onCancel: () -> Unit, onBookingClick: (EmployeeBookingItem) -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(50.dp).padding(end = 12.dp).padding(top = 2.dp)
        ) {
            Text(time, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }

        Box(modifier = Modifier.fillMaxHeight().width(20.dp), contentAlignment = Alignment.TopCenter) {
            Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(Color.Gray.copy(alpha = 0.2f)))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .padding(top = 4.dp)
                    .clip(CircleShape)
                    .background(if (booking != null) Color(0xFFEBC14F) else Color(0xFF2C2C2C))
                    .border(1.dp, if (booking != null) Color(0xFFEBC14F) else Color.Gray.copy(alpha = 0.5f), CircleShape)
            )
        }

        Column(modifier = Modifier.weight(1f).padding(bottom = 20.dp)) {
            if (booking == null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.1f))
                ) {
                    Text(
                        "Trống",
                        color = Color.Gray.copy(alpha = 0.4f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            } else {
                BookingCardList(booking, onConfirm, onCancel, onClick = { onBookingClick(booking) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailSheet(booking: EmployeeBookingItem, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp).padding(bottom = 40.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Chi tiết lịch hẹn", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, null, tint = Color.Gray) }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Thông tin khách hàng
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color(0xFFEBC14F)), contentAlignment = Alignment.Center) {
                    Text(booking.customerName.first().toString(), color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(booking.customerName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(booking.phone, color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(modifier = Modifier.size(45.dp), shape = CircleShape, color = Color(0xFF2C2C2C)) {
                    IconButton(onClick = {}) { Icon(Icons.Default.Call, null, tint = Color(0xFFEBC14F)) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(24.dp))

            DetailItem("Dịch vụ", booking.serviceName)
            DetailItem("Thời gian", "${booking.time} - ${booking.date}")
            DetailItem("Thời lượng dự kiến", "${booking.duration} phút")
            DetailItem("Giá dịch vụ", booking.price)
            DetailItem("Ghi chú", booking.note.ifEmpty { "Không có ghi chú" })

            Spacer(modifier = Modifier.height(32.dp))

            if (booking.status == "Pending") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Xác nhận lịch", color = Color.Black, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun StatusBadge(status: String) {
    val (color, text) = when (status) {
        "Pending" -> Color(0xFFEBC14F) to "Chờ xác nhận"
        "Confirmed" -> Color(0xFF4CAF50) to "Đã xác nhận"
        "Completed" -> Color(0xFF4CAF50) to "Hoàn thành"
        "Cancelled" -> Color(0xFFF44336) to "Đã hủy"
        else -> Color.Gray to status
    }
    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
        Text(text, color = color, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
