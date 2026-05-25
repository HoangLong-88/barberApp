package com.example.barberapp.Helps

import java.text.NumberFormat
import java.util.Locale

fun setVNDFormatString(obj : Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return "${formatter.format(obj  )} VND"
}