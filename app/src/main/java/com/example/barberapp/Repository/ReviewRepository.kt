package com.example.barberapp.Repository

import androidx.compose.runtime.Composable
import com.example.barberapp.Model.entities.Review
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class ReviewRepository {
    private val store = FirebaseFirestore.getInstance()

    fun submitReview(
        review: Review,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val shopRef = store.collection("shops").document(review.shopId)
        val reviewsRef = shopRef.collection("reviews")

        val docRef = if (review.id.isNotBlank()) {
            reviewsRef.document(review.id)
        } else {
            reviewsRef.document()
        }
        val reviewToSave = review.copy(id = docRef.id)

        docRef.set(reviewToSave)
            .addOnSuccessListener {
                // Bước 2: Tính lại rating trung bình từ toàn bộ reviews
                reviewsRef.get()
                    .addOnSuccessListener { // Tính lại rating trung bình
                        reviewsRef.get().addOnSuccessListener { snapshot ->
                            val avg = snapshot.documents
                                .mapNotNull { it.toObject(Review::class.java)?.rating?.toDouble() }
                                .average()
                                .takeIf { !it.isNaN() } ?: 0.0
                            shopRef.update("rating", avg)
                                .addOnCompleteListener { onSuccess() }
                        }.addOnFailureListener { onSuccess() }
                    }
                    .addOnFailureListener { e -> onError(e) }
            }
    }
}
