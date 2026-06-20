package com.example.barberapp.View.screenUI.customer.reviews

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.BorderActive
import com.example.barberapp.View.utils.BorderInactive
import com.example.barberapp.View.utils.StarRatingRow
import com.example.barberapp.View.utils.SurfaceDarker
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary
import com.example.barberapp.View.utils.YellowDim
import com.example.barberapp.View.utils.YellowPrimary
import com.example.barberapp.ViewModel.ReviewVM

// ─── WriteReviewScreen ────────────────────────────────────────────────────────

@Composable
fun WriteReviewScreen(
    shopId: String,
    userId: String,
    userName: String,
    reviewVM: ReviewVM = viewModel(),
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val existingReview by reviewVM.existingReview.collectAsState()
    // ── Local UI state ──────────────────────────────────────────────────────
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    val isActive = rating > 0 || reviewText.isNotBlank()
    val submitEnabled = rating > 0

    // ── Observe submit state từ ViewModel ──────────────────────────────────
    val submitState by reviewVM.submitState.collectAsState()
    val isLoading = submitState is ReviewVM.SubmitState.Loading
    val snackbarHostState = remember { SnackbarHostState() }
    // Fetch review cũ 1 lần khi mở màn hình
    LaunchedEffect(shopId, userId) {
        reviewVM.loadExistingReview(shopId, userId)
    }

    // Khi existingReview load xong → pre-fill vào state
    LaunchedEffect(existingReview) {
        existingReview?.let {
            rating     = it.rating.toInt()
            reviewText = it.comment
        }
    }
    // Xử lý kết quả submit
    LaunchedEffect(submitState) {
        when (val s = submitState) {
            is ReviewVM.SubmitState.Success -> {
                reviewVM.resetSubmitState()
                onSuccess()
                onBack()
            }

            is ReviewVM.SubmitState.Error -> {
                snackbarHostState.showSnackbar(
                    message = "Gửi thất bại: ${s.message}",
                    duration = SnackbarDuration.Short
                )
                reviewVM.resetSubmitState()
            }

            else -> Unit
        }
    }
    DisposableEffect(Unit) {
        onDispose { reviewVM.clearExistingReview() }
    }

    Scaffold(
        containerColor = BackgroundDark,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            WriteReviewTopBar(onBack = onBack)
        },
        bottomBar = {
            SubmitButton(
                enabled = submitEnabled && !isLoading,
                isLoading = isLoading,
                onClick = {
                    reviewVM.submitReview(
                        shopId = shopId,
                        userId = userId,
                        userName = userName,
                        rating = rating,
                        comment = reviewText.trim()
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Trải nghiệm của bạn như thế nào?",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))


            Spacer(modifier = Modifier.height(28.dp))

            // Star Rating Row
            StarRatingRow(
                rating = rating,
                onRating = { rating = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Review text field label
            Text(
                text = "Your Review",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Review TextField
            ReviewTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                isActive = isActive
            )
        }
    }
}

// ─── Top Bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WriteReviewTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Đánh giá",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundDark
        )
    )
}


// ─── Review Text Field ────────────────────────────────────────────────────────

@Composable
private fun ReviewTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isActive: Boolean
) {
    val borderColor by animateColorAsState(
        targetValue = if (isActive) BorderActive else BorderInactive,
        animationSpec = tween(300),
        label = "borderColor"
    )

    BasicTextField_Compat(
        value = value,
        onValueChange = onValueChange,
        borderColor = borderColor,
        placeholder = "Dịch vụ tuyệt mà barber còn nhiệt tình và thân thiện nữa!"
    )
}

@Composable
private fun BasicTextField_Compat(
    value: String,
    onValueChange: (String) -> Unit,
    borderColor: Color,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDarker) // Thay bằng màu background của bạn
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp), // Padding này áp dụng đều cho cả placeholder và vùng nhập chữ
        textStyle = LocalTextStyle.current.copy(
            fontSize = 14.sp,
            color = TextPrimary // Thay bằng màu chữ của bạn
        ),
        cursorBrush = SolidColor(YellowPrimary), // Màu của thanh dọc nhấp nháy
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart // Ép chữ luôn bắt đầu từ góc trên cùng bên trái
            ) {
                // Hiển thị placeholder nếu chưa nhập gì
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TextSecondary, // Thay bằng màu chữ phụ của bạn
                        fontSize = 14.sp
                    )
                }
                // Đây là component vẽ ra thanh nhấp nháy và vùng text của Compose
                innerTextField()
            }
        }
    )
}

// ─── Submit Button ────────────────────────────────────────────────────────────

@Composable
private fun SubmitButton(
    enabled: Boolean,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val buttonColor by animateColorAsState(
        targetValue = if (enabled) YellowPrimary else YellowDim,
        animationSpec = tween(300),
        label = "buttonColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                disabledContainerColor = YellowDim,
                contentColor = Color.Black,
                disabledContentColor = Color(0xFF5A4D00)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.Black,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Gửi Đánh Giá",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) Color.Black else Color(0xFF5A4D00)
                )
            }
        }
    }
}
