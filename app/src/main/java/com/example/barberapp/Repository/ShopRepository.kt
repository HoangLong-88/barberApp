package com.example.barberapp.Repository

import com.example.barberapp.Model.entities.Review
import com.example.barberapp.Model.entities.Shop
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ShopRepository {
    private val store = FirebaseFirestore.getInstance()
    suspend fun getAllShopData(): List<Shop>{
        return try {
            val querySnapshot = store.collection("shops").get().await()
            querySnapshot.documents.mapNotNull {
                document -> document.toObject(Shop::class.java)
            }
        }catch (e: Exception){
            println("Error Shop List Data: $e")
            emptyList()
        }
    }

    // Hàm 1: Chỉ lấy thông tin Shop (Không kèm review)
    fun getShopDetailData(shopId: String, onSuccess: (Shop?) -> Unit) {
        store.collection("shops").document(shopId)
            .get()
            .addOnSuccessListener { document ->
                val shop = document.toObject(Shop::class.java)
                onSuccess(shop)
            }
    }

    fun getReviewsForShop(shopId: String, onSuccess: (List<Review>) -> Unit) {
        store.collection("shops").document(shopId).collection("reviews")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Sắp xếp review mới nhất lên đầu
            .get()
            .addOnSuccessListener { snapshot ->
                val reviews = snapshot.toObjects(Review::class.java)
                onSuccess(reviews)
            }
    }
}