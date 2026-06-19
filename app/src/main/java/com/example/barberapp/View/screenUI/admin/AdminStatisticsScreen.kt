package com.example.barberapp.View.screenUI.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.ViewModel.AdminViewModel

@Composable
fun AdminStatisticsScreen(viewModel: AdminViewModel) {
    // Sử dụng 'by' cho các MutableState đơn lẻ (đúng)
    val totalRevenue by viewModel.totalRevenue
    val totalBookings by viewModel.totalBookingsCount
    val avgRevenue by viewModel.avgRevenuePerBooking
    val avgBookingsPerStaff by viewModel.avgBookingsPerStaff
    val completionRate by viewModel.completionRate
    val statsTimeRange by viewModel.statsTimeRange
    
    // SỬA LỖI: Sử dụng '=' cho các danh sách (SnapshotStateList không dùng được 'by')
    val popularServices = viewModel.popularServices
    val staffPerformance = viewModel.staffPerformance

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 20.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Báo cáo thống kê", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                IconButton(
                    onClick = { viewModel.refreshData() }, 
                    modifier = Modifier.size(36.dp).background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.Refresh, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(20.dp))
                }
            }
        }

        // Time Range Filter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Hôm nay", "Tuần", "Tháng", "Năm").forEach { range ->
                    Surface(
                        modifier = Modifier.weight(1f).clickable { viewModel.setStatsTimeRange(range) },
                        color = if (statsTimeRange == range) Color(0xFFEBC14F) else Color(0xFF1E1E1E),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            range,
                            modifier = Modifier.padding(8.dp).fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = if (statsTimeRange == range) Color.Black else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Stat Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Doanh thu", "${String.format("%,d", totalRevenue)}đ", Icons.Default.AttachMoney, Color(0xFFEBC14F), Modifier.weight(1f))
                    StatCard("Tổng lịch", totalBookings.toString(), Icons.Default.DateRange, Color(0xFF2196F3), Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Đ.Thu TB", "${String.format("%,d", avgRevenue)}đ", Icons.Default.TrendingUp, Color(0xFF4CAF50), Modifier.weight(1f))
                    StatCard("Lịch/NV", String.format("%.1f", avgBookingsPerStaff), Icons.Default.Person, Color(0xFFFF9800), Modifier.weight(1f))
                }
            }
        }

        // Popular Services
        item { SectionHeader("Dịch vụ phổ biến nhất") }
        item {
            if (popularServices.isEmpty()) {
                EmptyStateCard("Chưa có dữ liệu dịch vụ", Icons.Default.Info)
            } else {
                Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        popularServices.forEachIndexed { index, pair ->
                            val progressValue = if (totalBookings > 0) pair.second.toFloat() / totalBookings else 0f
                            ServiceStatRow(pair.first, pair.second, progressValue, index == popularServices.size - 1)
                        }
                    }
                }
            }
        }

        // Staff Performance
        item { SectionHeader("Hiệu suất nhân viên") }
        if (staffPerformance.isEmpty()) {
            item { EmptyStateCard("Chưa có dữ liệu nhân viên", Icons.Default.Warning) }
        } else {
            // SỬA LỖI: Chỉ rõ kiểu dữ liệu Pair để tránh lỗi giải nén
            items(staffPerformance) { pair: Pair<String, Int> ->
                val (name, count) = pair
                StaffStatCard(name, count)
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(36.dp).background(color.copy(0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ServiceStatRow(name: String, count: Int, progress: Float, isLast: Boolean) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, color = Color.White, fontSize = 14.sp)
            Text("$count lượt", color = Color(0xFFEBC14F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(6.dp),
            color = Color(0xFFEBC14F),
            trackColor = Color(0xFF2C2C2C),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        if (!isLast) Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StaffStatCard(name: String, count: Int) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Phục vụ $count khách hàng", color = Color.Gray, fontSize = 12.sp)
            }
            Icon(Icons.Default.Star, null, tint = Color(0xFFEBC14F), modifier = Modifier.size(16.dp))
            Text(" 4.9", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun EmptyStateCard(message: String, icon: ImageVector) {
    Surface(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
            Text(message, color = Color.Gray, fontSize = 14.sp, textAlign = TextAlign.Center)
        }
    }
}
