package com.example.barberapp.Model.entities

import com.example.barberapp.View.screenUI.customer.bookings.BookingStatus
import com.google.firebase.firestore.DocumentId

data class Booking(
    @DocumentId val id: String = "",
    val userId: String = "",               // ID khách hàng đặt lịch
    val shopId: String = "",               // ID tiệm cắt tóc
    val shopName: String = "",             // Tên tiệm lúc đặt để hiển thị nhanh ở Lịch sử
    val barberId: String = "",             // ID thợ được chọn (nếu có)
    val barberName: String = "",           // Tên thợ
    val services: List<BookingService> = emptyList(), // Danh sách các dịch vụ được chọn
    val totalPrice: Long = 0L,             // Tổng tiền
    val bookingDate: String = "",          // Ngày hẹn (Ví dụ: "15/06/2026")
    val bookingTime: String = "",          // Giờ hẹn (Ví dụ: "14:30")
    var status: BookingStatus = BookingStatus.Pending,        // Trạng thái: PENDING, CONFIRMED, COMPLETED, CANCELLED
    val createdAt: Long = System.currentTimeMillis()
)

data class BookingService(
    val serviceId: String = "",
    val name: String ="",
    val price: Long = 0L,
    val duration: String = ""
)