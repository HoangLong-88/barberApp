package com.example.barberapp.Repository

import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Review
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
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

    // Hàm 1: Chỉ lấy thông tin Shop (Không kèm review)
    fun getShopDetailData(shopId: String, onSuccess: (Shop?) -> Unit) {
        val shopDocRef = store.collection("shops").document(shopId)

        shopDocRef.get().addOnSuccessListener { shopSnapshot ->
            val selectedShop = shopSnapshot.toObject(Shop::class.java)

            if (selectedShop != null) {
                // Lấy Services từ Root Collection "services" có shopId trùng khớp
                store.collection("services").whereEqualTo("shopId", shopId).get()
                    .addOnSuccessListener { serviceSnap ->
                        val servicesList = serviceSnap.documents.mapNotNull { d ->
                            d.toObject(Service::class.java)?.copy(id = d.id)
                        }

                        // Lấy Barbers từ Root Collection "barbers" có shopId trùng khớp
                        store.collection("barbers").whereEqualTo("shopId", shopId).get()
                            .addOnSuccessListener { barberSnap ->
                                val barbersList = barberSnap.documents.mapNotNull { d ->
                                    d.toObject(Employee::class.java)?.copy(id = d.id)
                                }

                                // Gộp dữ liệu lại trả về cho View
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