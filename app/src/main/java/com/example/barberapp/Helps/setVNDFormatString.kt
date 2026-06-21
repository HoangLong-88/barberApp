package com.example.barberapp.Helps

import java.text.NumberFormat
import java.util.Locale

/**
 * Chuyển đổi giá trị số sang định dạng tiền tệ VND.
 * Sử dụng kiểu Number để hỗ trợ cả Int (từ Service) và Long (từ Booking).
 */
fun setVNDFormatString(obj: Number): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return "${formatter.format(obj)} VND"
}
