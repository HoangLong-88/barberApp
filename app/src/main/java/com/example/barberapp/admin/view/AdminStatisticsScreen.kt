package com.example.barberapp.admin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.admin.viewmodel.AdminViewModel

@Composable
fun AdminStatisticsScreen(viewModel: AdminViewModel) {
    val totalRevenue by viewModel.totalRevenue
    val totalBookings by viewModel.totalBookingsCount
    val popularServices by viewModel.popularServices
    val staffPerformance by viewModel.staffPerformance

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        // 1. Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Doanh thu",
                    value = "${String.format("%,d", totalRevenue)}đ",
                    icon = Icons.Default.AttachMoney,
                    containerColor = Color(0xFFEBC14F).copy(0.1f),
                    contentColor = Color(0xFFEBC14F),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Tổng lịch",
                    value = totalBookings.toString(),
                    icon = Icons.Default.DateRange,
                    containerColor = Color(0xFF2196F3).copy(0.1f),
                    contentColor = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 2. Popular Services Section
        item {
            SectionHeader("Dịch vụ phổ biến nhất")
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (popularServices.isEmpty()) {
                        Text("Chưa có dữ liệu", color = Color.Gray, fontSize = 14.sp)
                    } else {
                        popularServices.forEachIndexed { index, pair ->
                            val progressValue = if (totalBookings > 0) pair.second.toFloat() / totalBookings else 0f
                            ServiceStatRow(
                                name = pair.first,
                                count = pair.second,
                                progress = progressValue,
                                isLast = index == popularServices.size - 1
                            )
                        }
                    }
                }
            }
        }

        // 3. Staff Performance Section
        item {
            SectionHeader("Hiệu suất nhân viên")
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(staffPerformance) { (name, count) ->
            StaffStatCard(name, count)
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(containerColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = contentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ServiceStatRow(name: String, count: Int, progress: Float, isLast: Boolean) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text("$count lượt", color = Color(0xFFEBC14F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = Color(0xFFEBC14F),
            trackColor = Color(0xFF2C2C2C),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        if (!isLast) {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StaffStatCard(name: String, bookingCount: Int) {
    Surface(
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Phục vụ $bookingCount khách hàng", color = Color.Gray, fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(16.dp))
                Text(" 4.9", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}
