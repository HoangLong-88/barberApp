package com.example.barberapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barberapp.Model.entities.Review
import com.example.barberapp.Repository.ReviewRepository
import com.example.barberapp.Repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewVM: ViewModel() {
    private val reviewRepo = ReviewRepository()
    private val shopRepo = ShopRepository()

    // Trạng thái submit review: idle / loading / success / error
    sealed class SubmitState {
        object Idle    : SubmitState()
        object Loading : SubmitState()
        object Success : SubmitState()
        data class Error(val message: String) : SubmitState()
    }
    private val _submitState = MutableStateFlow<SubmitState>(SubmitState.Idle)
    val submitState: StateFlow<SubmitState> = _submitState.asStateFlow()
    // Thêm vào ReviewVM
    private val _existingReview = MutableStateFlow<Review?>(null)
    val existingReview: StateFlow<Review?> = _existingReview.asStateFlow()

    fun loadExistingReview(shopId: String, userId: String) {
        viewModelScope.launch {
            shopRepo.getReviewByUser(shopId, userId) { review ->
                _existingReview.value = review
            }
        }
    }

    fun clearExistingReview() {
        _existingReview.value = null
    }
    fun submitReview(
        shopId   : String,
        userId   : String,
        userName : String,
        rating   : Int,
        comment  : String,
    ) {
        _submitState.value = SubmitState.Loading
        val review = Review(
            id = _existingReview.value?.id?:"",
            shopId = shopId,
            userId = userId,
            userName = userName,
            rating = rating.toFloat(),
            comment = comment,
            timestamp = System.currentTimeMillis()
        )
        reviewRepo.submitReview(
            review    = review,
            onSuccess = {
                _submitState.value = SubmitState.Success
            },
            onError = { e ->
                _submitState.value = SubmitState.Error(e.message ?: "Đã xảy ra lỗi")
            }
        )
    }
    fun resetSubmitState() {
        _submitState.value = SubmitState.Idle
    }
}