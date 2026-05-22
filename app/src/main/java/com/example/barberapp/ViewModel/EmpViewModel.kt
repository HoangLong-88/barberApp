package com.example.barberapp.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.barberapp.Model.entities.DateItem
import com.example.barberapp.Model.entities.EmployeeBookingItem
import com.example.barberapp.Model.entities.EmployeeInfo
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class EmpViewModel : ViewModel() {

    // --- UI State ---
    private val _currentTab = mutableStateOf("Lịch")
    val currentTab: State<String> = _currentTab

    private val _viewMode = mutableStateOf("Timeline")
    val viewMode: State<String> = _viewMode

    private val _selectedDate = mutableStateOf(LocalDate.now())
    val selectedDate: State<LocalDate> = _selectedDate

    val employeeInfo = mutableStateOf(
        EmployeeInfo(
            name = "Thế Anh",
            rating = 4.9,
            totalRatings = 128,
            appointmentsToday = 5,
            completedToday = 2
        )
    )

    val dateList = mutableStateListOf<DateItem>()
    val bookings = mutableStateListOf<EmployeeBookingItem>()

    // Lọc danh sách booking theo ngày đã chọn (Business Logic)
    val filteredBookings = derivedStateOf {
        bookings.filter { it.date == _selectedDate.value.toString() }
    }

    init {
        generateDates()
        loadSampleBookings()
    }

    private fun generateDates() {
        dateList.clear()
        val today = LocalDate.now()
        val vietnameseLocale = Locale("vi", "VN")

        for (i in 0..6) {
            val date = today.plusDays(i.toLong())
            val dayOfWeek = if (date == today) "H.Nay" else {
                val dow = date.dayOfWeek.getDisplayName(TextStyle.SHORT, vietnameseLocale)
                when (dow) {
                    "Th 2" -> "T2"
                    "Th 3" -> "T3"
                    "Th 4" -> "T4"
                    "Th 5" -> "T5"
                    "Th 6" -> "T6"
                    "Th 7" -> "T7"
                    "CN" -> "CN"
                    else -> dow
                }
            }
            dateList.add(
                DateItem(
                    dayOfWeek = dayOfWeek,
                    dayOfMonth = date.dayOfMonth.toString(),
                    fullDate = date.toString(),
                    isSelected = date == today
                )
            )
        }
    }

    private fun loadSampleBookings() {
        bookings.clear()
        val today = LocalDate.now()

        bookings.addAll(listOf(
            EmployeeBookingItem(
                "1",
                "Nguyễn Văn A",
                "Cắt tóc nam",
                "100.000đ",
                "09:00",
                30,
                today.toString(),
                "Confirmed",
                "0987654321",
                "Cắt ngắn hai bên"
            ),
            EmployeeBookingItem(
                "2",
                "Trần Thị B",
                "Gội đầu",
                "150.000đ",
                "11:00",
                45,
                today.toString(),
                "Pending",
                "0123456789",
                ""
            ),
            EmployeeBookingItem(
                "3",
                "Lê Văn C",
                "Combo Uốn",
                "350.000đ",
                "14:00",
                90,
                today.toString(),
                "Pending",
                "0333444555",
                "Uốn kiểu layer"
            ),
            EmployeeBookingItem(
                "4",
                "Hoàng An",
                "Cạo mặt",
                "80.000đ",
                "10:00",
                20,
                today.plusDays(1).toString(),
                "Pending",
                "0999888777",
                ""
            ),
            EmployeeBookingItem(
                "5",
                "Minh Tuấn",
                "Tỉa râu",
                "50.000đ",
                "15:00",
                15,
                today.plusDays(1).toString(),
                "Confirmed",
                "0777666555",
                ""
            ),
            EmployeeBookingItem(
                "6",
                "Quốc Bảo",
                "Nhuộm tóc",
                "450.000đ",
                "09:00",
                120,
                today.plusDays(2).toString(),
                "Pending",
                "0888777666",
                "Nhuộm màu xám khói"
            )
        ))
    }

    // --- Actions ---
    fun setTab(tab: String) { _currentTab.value = tab }
    fun setViewMode(mode: String) { _viewMode.value = mode }

    fun selectDate(dateItem: DateItem) {
        _selectedDate.value = LocalDate.parse(dateItem.fullDate)
        val newList = dateList.map { it.copy(isSelected = it.fullDate == dateItem.fullDate) }
        dateList.clear()
        dateList.addAll(newList)
    }

    fun confirmBooking(id: String) {
        val index = bookings.indexOfFirst { it.id == id }
        if (index != -1) {
            bookings[index] = bookings[index].copy(status = "Confirmed")
        }
    }

    fun cancelBooking(id: String) {
        val index = bookings.indexOfFirst { it.id == id }
        if (index != -1) {
            bookings[index] = bookings[index].copy(status = "Cancelled")
        }
    }
}