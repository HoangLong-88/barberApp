package com.example.barberapp.Repository

import com.example.barberapp.Model.entities.Booking
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BookingRepository {
    private val firestore = FirebaseFirestore.getInstance()
    fun fetchAllBookingState(userId: String): Flow<List<Booking>> = callbackFlow {
        val listener = firestore.collection("bookings")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val result = snapshot?.documents?.mapNotNull { d ->
                    d.toObject(Booking::class.java)?.copy(id = d.id)
                } ?: emptyList()
                trySend(result)
            }
        awaitClose { listener.remove() }  // ← tự cleanup khi VM bị clear
    }
    fun fetchBookingsForBarber(barberId: String): Flow<List<Booking>> = callbackFlow {
        val listener = firestore.collection("bookings")
            .whereEqualTo("barberId", barberId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val result = snapshot?.documents?.mapNotNull { d ->
                    d.toObject(Booking::class.java)?.copy(id = d.id)
                } ?: emptyList()
                trySend(result)
            }
        awaitClose { listener.remove() }
    }
}