package com.example.barberapp.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.Model.entities.DateItem
import com.example.barberapp.Model.entities.EmployeeInfo
import com.example.barberapp.Repository.BookingRepository
import com.example.barberapp.View.screenUI.customer.bookings.BookingStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class EmpViewModel : ViewModel() {

    private val bookingRepo = BookingRepository()
    private val firestore = FirebaseFirestore.getInstance()
    private val vnDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy") // khớp format lưu ở BookingCheckoutActivity

    // --- UI State ---
    private val _currentTab = mutableStateOf("Lịch")
    val currentTab: State<String> = _currentTab

    private val _viewMode = mutableStateOf("Timeline")
    val viewMode: State<String> = _viewMode

    private val _selectedDate = mutableStateOf(LocalDate.now())
    val selectedDate: State<LocalDate> = _selectedDate

    val employeeInfo = mutableStateOf(EmployeeInfo(name = "Nhân viên", rating = 5.0))

    val dateList = mutableStateListOf<DateItem>()

    // Booking THẬT lấy từ Firestore, lọc theo barberId = uid nhân viên đang đăng nhập
    val bookings = mutableStateListOf<Booking>()
    private var currentBarberId: String = ""

    val filteredBookings = derivedStateOf {
        val selected = _selectedDate.value
        bookings.filter { parseBookingDate(it.bookingDate) == selected }
            .sortedBy { it.bookingTime }
    }

    init {
        generateDates()
    }

    /** Gọi khi biết uid của Barber đang đăng nhập (truyền từ EmployeeScreen). */
    fun loadBookings(barberId: String) {
        if (barberId.isBlank() || barberId == currentBarberId) return
        currentBarberId = barberId
        viewModelScope.launch {
            bookingRepo.fetchBookingsForBarber(barberId).collect { result ->
                bookings.clear()
                bookings.addAll(result)
                recalcStats()
            }
        }
    }

    fun setEmployeeProfile(name: String) {
        if (name.isBlank() || name == employeeInfo.value.name) return
        employeeInfo.value = employeeInfo.value.copy(name = name)
    }

    private fun recalcStats() {
        val today = LocalDate.now()
        val todayBookings = bookings.filter { parseBookingDate(it.bookingDate) == today }
        employeeInfo.value = employeeInfo.value.copy(
            appointmentsToday = todayBookings.size,
            completedToday = todayBookings.count { it.status == BookingStatus.Completed }
        )
    }

    private fun parseBookingDate(raw: String): LocalDate? = try {
        LocalDate.parse(raw, vnDateFormat)
    } catch (e: Exception) {
        null
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
                    "Th 2" -> "T2"; "Th 3" -> "T3"; "Th 4" -> "T4"
                    "Th 5" -> "T5"; "Th 6" -> "T6"; "Th 7" -> "T7"
                    "CN" -> "CN"; else -> dow
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

    // --- Actions ---
    fun setTab(tab: String) { _currentTab.value = tab }
    fun setViewMode(mode: String) { _viewMode.value = mode }

    fun selectDate(dateItem: DateItem) {
        _selectedDate.value = LocalDate.parse(dateItem.fullDate)
        val newList = dateList.map { it.copy(isSelected = it.fullDate == dateItem.fullDate) }
        dateList.clear()
        dateList.addAll(newList)
    }

    /** Xác nhận booking — giống Admin: chuyển trạng thái sang Completed */
    fun confirmBooking(bookingId: String) {
        firestore.collection("bookings").document(bookingId)
            .update("status", BookingStatus.Completed.name)
    }

    /** Hủy booking — giống Admin: chuyển trạng thái sang Cancelled */
    fun cancelBooking(bookingId: String) {
        firestore.collection("bookings").document(bookingId)
            .update("status", BookingStatus.Cancelled.name)
    }
}