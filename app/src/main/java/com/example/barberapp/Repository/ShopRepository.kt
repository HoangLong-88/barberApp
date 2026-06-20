package com.example.barberapp.Repository

import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Review
import com.example.barberapp.Model.entities.Service
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.Model.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class ShopRepository {
    private val store = FirebaseFirestore.getInstance()
    fun getAllShopData(userId: String, onSuccess: (List<Shop?>) -> Unit = {}) {
        // Lấy favoriteShopIds của user trước
        getFavoriteShopIds(userId) { favoriteIds ->
            store.collection("shops").get(Source.SERVER)
                .addOnSuccessListener { snapshot ->
                    val shops = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(Shop::class.java)
                                ?.copy(
                                    id         = doc.id,
                                    isFavorite = favoriteIds.contains(doc.id)  // map đúng theo user
                                )
                        } catch (e: Exception) { null }
                    }
                    onSuccess(shops)
                }
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
                                            totalRatings = 5.0
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
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
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
    fun getReviewByUser(shopId: String, userId: String, onResult: (Review?) -> Unit) {
        store.collection("shops").document(shopId)
            .collection("reviews")
            .whereEqualTo("userId", userId)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val review = snapshot.documents.firstOrNull()
                    ?.toObject(Review::class.java)
                    ?.copy(id = snapshot.documents.first().id)
                onResult(review)
            }
            .addOnFailureListener { onResult(null) }
    }
    fun getFavoriteShopIds(userId: String, onResult: (List<String>) -> Unit) {
        store.collection("users").document(userId).get(Source.SERVER)
            .addOnSuccessListener { doc ->
                val ids = doc.get("favoriteShopIds") as? List<String> ?: emptyList()
                onResult(ids)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun updateFavoriteStatus(
        userId: String,
        shopId: String,
        isFav: Boolean,
        onComplete: (Boolean) -> Unit = {}
    ) {
        val userRef = store.collection("users").document(userId)
        store.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val current = snapshot.get("favoriteShopIds") as? List<String> ?: emptyList()
            val updated = if (isFav) {
                (current + shopId).distinct()
            } else {
                current - shopId
            }
            transaction.update(userRef, "favoriteShopIds", updated)
        }
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}