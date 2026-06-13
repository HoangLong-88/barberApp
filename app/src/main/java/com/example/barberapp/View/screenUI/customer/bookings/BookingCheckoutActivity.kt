package com.example.barberapp.View.screenUI.customer.bookings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.Model.entities.BookingService
import com.example.barberapp.View.screenUI.customer.home.CardDark
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.ViewModel.ShopVM
import com.google.firebase.firestore.FirebaseFirestore

@OptIn( ExperimentalMaterial3Api::class)
@Composable
fun BookingCheckoutScreen(
    navController: NavController,
    shopId: String,
    initialServiceIds: List<String>,
    shopVM: ShopVM = viewModel()
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    // Đọc thông tin Shop hiện tại từ ViewModel
    val shopState by shopVM.shop.collectAsState()

    // Trạng thái danh sách dịch vụ đang được lựa chọn (Có thể bị xóa bớt)
    var selectedServices by remember { mutableStateOf(listOf<BookingService>()) }
    var isLoading by remember { mutableStateOf(true) }

    // Gọi lấy dữ liệu chi tiết Shop nếu chưa có sẵn dữ liệu trong State
    LaunchedEffect(shopId) {
        shopVM.loadShopDetails(shopId)
    }

    // Đồng bộ và lọc lấy thông tin các Service tương ứng từ IDs được truyền sang
    LaunchedEffect(shopState) {
        shopState?.let { shop ->
            val filtered = shop.services.filter { initialServiceIds.contains(it.id) }.map {
                BookingService(
                    serviceId = it.id,
                    name = it.name,
                    price = it.price.toLong(),
                    duration = it.duration
                )
            }
            selectedServices = filtered
            isLoading = false
        }
    }

    // TÍNH TOÁN BILL ĐỘNG: Mỗi khi selectedServices thay đổi, biến này tự tính toán lại
    val totalPrice = remember(selectedServices) {
        selectedServices.sumOf { it.price }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Xác nhận đặt lịch", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFEBC14F))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. THÔNG TIN TIỆM
                    item {
                        Text(
                            text = shopState?.name ?: "Barber Shop",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(text = shopState?.address ?: "", color = Color.Gray, fontSize = 14.sp)
                    }

                    // 2. DANH SÁCH DỊCH VỤ ĐÃ CHỌN (DẠNG DISMISSIBLE CHIPS)
                    item {
                        Text("Dịch vụ đã chọn:", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))

                        if (selectedServices.isEmpty()) {
                            Text("Chưa chọn dịch vụ nào. Vui lòng quay lại.", color = Color.Red, fontSize = 14.sp)
                        } else {
                            // Hiển thị dạng thanh cuộn ngang chứa các Chip xóa được
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(selectedServices, key = { it.serviceId }) { service ->
                                    InputChip(
                                        selected = true,
                                        onClick = { /* Không làm gì khi bấm vào thân chip */ },
                                        label = { Text(service.name, color = Color.White) },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = {
                                                    // CHỨC NĂNG HỦY: Click dấu X loại bỏ khỏi danh sách được chọn
                                                    selectedServices = selectedServices.filter { it.serviceId != service.serviceId }
                                                },
                                                modifier = Modifier.size(16.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Xóa",
                                                    tint = Color.Black
                                                )
                                            }
                                        },
                                        colors = InputChipDefaults.inputChipColors(
                                            containerColor = Color(0xFFEBC14F),
                                            labelColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 3. SUMMARIES BILL DỰA TRÊN TRẠNG THÁI HIỆN TẠI
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Chi tiết hóa đơn", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(Modifier.height(12.dp))

                                // Liệt kê từng dịch vụ và giá động
                                selectedServices.forEach { service ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(service.name, color = Color.LightGray, fontSize = 14.sp)
                                        Text("%,d VND".format(service.price), color = Color.White, fontSize = 14.sp)
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.DarkGray)

                                // Tổng số tiền
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Tổng cộng", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(
                                        text = "%,d VND".format(totalPrice),
                                        color = Color(0xFFEBC14F),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }

                // 4. NÚT CONFIRM BOOKING CUỐI CÙNG LƯU VÀO FIREBASE
                Button(
                    onClick = {
                        if (selectedServices.isEmpty()) {
                            Toast.makeText(context, "Vui lòng chọn ít nhất 1 dịch vụ!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Tạo Object Booking hoàn chỉnh
                        val newBooking = Booking(
                            userId = "CUSTOMER_CURRENT_ID", // Nên lấy từ FirebaseAuth.getInstance().currentUser?.uid
                            shopId = shopId,
                            shopName = shopState?.name ?: "",
                            services = selectedServices,
                            totalPrice = totalPrice,
                            bookingDate = "15/06/2026", // Tạm thời hardcode hoặc lấy từ lịch chọn ngày của bạn
                            bookingTime = "14:30",      // Tạm thời hardcode hoặc lấy từ lịch chọn giờ của bạn
                            status = BookingStatus.Pending
                        )

                        // Đẩy lên Firestore collection "bookings"
                        db.collection("bookings").add(newBooking)
                            .addOnSuccessListener { docRef ->
                                // Thành công: Chuyển hướng sang màn hình thông báo Đặt thành công
                                navController.navigate("booking_success/${docRef.id}") {
                                    // Xóa sạch hàng đợi màn hình trước đó để tránh bấm nút Back quay lại trang Checkout
                                    popUpTo("booking_checkout/{shopId}/{serviceIds}") { inclusive = true }
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Lỗi hệ thống: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEBC14F)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Xác nhận Đặt lịch", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}