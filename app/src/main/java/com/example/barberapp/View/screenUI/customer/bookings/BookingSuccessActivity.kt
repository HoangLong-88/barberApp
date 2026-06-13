package com.example.barberapp.View.screenUI.customer.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.View.screenUI.customer.home.CardDark
import com.example.barberapp.View.utils.BackgroundDark
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BookingSuccessScreen(navController: NavController, bookingId: String) {
    val db = FirebaseFirestore.getInstance()
    var bookingDetail by remember { mutableStateOf<Booking?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Đọc thông tin bill vừa tạo thành công từ Firestore về hiển thị
    LaunchedEffect(bookingId) {
        db.collection("bookings").document(bookingId).get()
            .addOnSuccessListener { snapshot ->
                bookingDetail = snapshot.toObject(Booking::class.java)
                isLoading = false
            }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFFEBC14F))
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon tích xanh hoàn thành lớn
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Đặt lịch thành công!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Mã hóa đơn: #$bookingId",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // BOX BILL SUMMARIES LẤY TỪ DATA MỚI NHẤT
                bookingDetail?.let { booking ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Tóm tắt dịch vụ",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Cửa hàng: ${booking.shopName}", color = Color.LightGray, fontSize = 14.sp)
                            Text(text = "Thời gian: ${booking.bookingTime} - ${booking.bookingDate}", color = Color.LightGray, fontSize = 14.sp)

                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.DarkGray)

                            // Danh sách các chip dịch vụ đã chốt
                            booking.services.forEach { service ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "- ${service.name}", color = Color.Gray, fontSize = 14.sp)
                                    Text(text = "%,d VND".format(service.price), color = Color.White, fontSize = 14.sp)
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.DarkGray)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Tổng tiền đã trả", color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = "%,d VND".format(booking.totalPrice),
                                    color = Color(0xFFEBC14F),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // NÚT QUAY VỀ TRANG CHỦ (BACK TO HOME)
                Button(
                    onClick = {
                        // Clear toàn bộ stack cũ để quay thẳng ra màn hình chính bảo mật an toàn
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Quay về trang chủ", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}