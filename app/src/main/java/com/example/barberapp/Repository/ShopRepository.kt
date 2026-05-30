package com.example.barberapp.Repository

import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Review
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ShopRepository {
    private val store = FirebaseFirestore.getInstance()
    fun getAllShopData(onSuccess: (List<Shop?>) -> Unit = {}) {
        store.collection("shops").get()
            .addOnSuccessListener { snapshot ->
                val shops = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Shop::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        null
                    }
                }
                onSuccess(shops)
            }
    }

    fun getShopDetailData(shopId: String, onSuccess: (Shop?) -> Unit) {
        val shopDocRef = store.collection("shops").document(shopId)

        shopDocRef.get().addOnSuccessListener { shopSnapshot ->
            val selectedShop = shopSnapshot.toObject(Shop::class.java)

            if (selectedShop != null) {
                // Lấy Services
                store.collection("services").whereEqualTo("shopId", shopId).get()
                    .addOnSuccessListener { serviceSnap ->
                        val servicesList = serviceSnap.documents.mapNotNull { d ->
                            d.toObject(Service::class.java)?.copy(id = d.id)
                        }

                        // Lấy Barbers TỪ BẢNG 'users' THAY VÌ BẢNG 'barbers'
                        store.collection("users")
                            .whereEqualTo("role", "employee")
                            .whereEqualTo("shopId", shopId)
                            .get()
                            .addOnSuccessListener { barberSnap ->
                                // Mapping User -> Employee
                                val barbersList = barberSnap.documents.mapNotNull { d ->
                                    val user = d.toObject(User::class.java)
                                    if (user != null) {
                                        Employee(
                                            id = d.id,
                                            shopId = user.shopId,
                                            name = user.name,
                                            avatarUrl = user.avatarUrl ?: "", // Lấy avatar,
                                            rating = 5.0, // Chỗ này có thể để default hoặc mapping thêm
                                            totalRatings = 0
                                        )
                                    } else null
                                }

                                onSuccess(
                                    selectedShop.copy(
                                        id = shopSnapshot.id,
                                        services = servicesList,
                                        barbers = barbersList
                                    )
                                )
                            }
                    }
            } else {
                onSuccess(null)
            }
        }.addOnFailureListener { onSuccess(null) }
    }

    fun getReviewsForShop(shopId: String, onSuccess: (List<Review>) -> Unit) {
        store.collection("shops").document(shopId).collection("reviews")
            .get()
            .addOnSuccessListener { snapshot ->
                val reviewList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Review::class.java)?.copy(id = doc.id)
                }
                onSuccess(reviewList)
            }
            .addOnFailureListener {
                onSuccess(emptyList())
            }
    }
}