package com.example.barberapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.Repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingVM : ViewModel(){
    private val bookingRepo = BookingRepository()
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    fun loadCustomerBookings(userId: String) {
        viewModelScope.launch {
            bookingRepo.fetchAllBookingState(userId).collect { result ->
                _bookings.value = result
            }
        }
    }
}