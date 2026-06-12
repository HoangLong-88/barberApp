package com.example.barberapp.View.screenUI.customer.reviews

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Color Palette ────────────────────────────────────────────────────────────
private val BackgroundDark  = Color(0xFF121212)
private val SurfaceDark     = Color(0xFF1E1E1E)
private val SurfaceDarker   = Color(0xFF1A1A1A)
private val YellowPrimary   = Color(0xFFF5C518)
private val YellowDim       = Color(0xFF8A7020)
private val TextPrimary     = Color(0xFFFFFFFF)
private val TextSecondary   = Color(0xFF9E9E9E)
private val StarEmpty       = Color(0xFF3A3A3A)
private val BorderActive    = Color(0xFFF5C518)
private val BorderInactive  = Color(0xFF2C2C2C)

// ─── WriteReviewScreen ────────────────────────────────────────────────────────

@Composable
fun WriteReviewScreen(
    shopName: String = "King Barber Shop",
    onBack: () -> Unit = {},
    onSubmit: (rating: Int, review: String) -> Unit = { _, _ -> }
) {
    // State
    var rating      by remember { mutableStateOf(0) }          // 0 = no rating yet
    var reviewText  by remember { mutableStateOf("") }
    val isActive    = rating > 0 || reviewText.isNotBlank()     // "active" state

    // Derived UI values
    val submitEnabled = rating > 0

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            WriteReviewTopBar(onBack = onBack)
        },
        bottomBar = {
            SubmitButton(
                enabled = submitEnabled,
                onClick  = { onSubmit(rating, reviewText) }
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
                text       = "How was your experience?",
                color      = TextPrimary,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Shop name
            Text(
                text     = "at $shopName",
                color    = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Star Rating Row
            StarRatingRow(
                rating    = rating,
                onRating  = { rating = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Review text field label
            Text(
                text      = "Your Review",
                color     = TextSecondary,
                fontSize  = 13.sp,
                modifier  = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Review TextField
            ReviewTextField(
                value       = reviewText,
                onValueChange = { reviewText = it },
                isActive    = isActive
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
                text       = "Write Review",
                color      = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector        = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint               = TextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundDark
        )
    )
}

// ─── Star Rating Row ──────────────────────────────────────────────────────────

@Composable
private fun StarRatingRow(
    rating   : Int,
    onRating : (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            StarItem(
                filled    = i <= rating,
                onClick   = { onRating(i) }
            )
        }
    }
}

@Composable
private fun StarItem(
    filled  : Boolean,
    onClick : () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (filled) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessMedium
        ),
        label = "starScale"
    )

    Icon(
        imageVector        = if (filled) Icons.Filled.Star else Icons.Outlined.StarOutline,
        contentDescription = "Star",
        tint               = if (filled) YellowPrimary else StarEmpty,
        modifier           = Modifier
            .size(44.dp)
            .scale(scale)
            .clickable(
                indication             = null,
                interactionSource      = remember { MutableInteractionSource() },
                onClick                = onClick
            )
    )
}

// ─── Review Text Field ────────────────────────────────────────────────────────

@Composable
private fun ReviewTextField(
    value          : String,
    onValueChange  : (String) -> Unit,
    isActive       : Boolean
) {
    val borderColor by animateColorAsState(
        targetValue   = if (isActive) BorderActive else BorderInactive,
        animationSpec = tween(300),
        label         = "borderColor"
    )

    BasicTextField_Compat(
        value         = value,
        onValueChange = onValueChange,
        borderColor   = borderColor,
        placeholder   = "Great haircut and friendly barber..."
    )
}

/**
 * Custom text field styled to match the dark barbershop aesthetic.
 * We use TextField under the hood but strip default decorations.
 */
@Composable
private fun BasicTextField_Compat(
    value         : String,
    onValueChange : (String) -> Unit,
    borderColor   : Color,
    placeholder   : String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDarker)
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text     = placeholder,
                color    = TextSecondary,
                fontSize = 14.sp
            )
        }
        TextField(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier.fillMaxSize(),
            colors        = TextFieldDefaults.colors(
                focusedContainerColor   = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor        = TextPrimary,
                unfocusedTextColor      = TextPrimary,
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor             = YellowPrimary
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                color    = TextPrimary
            )
        )
    }
}

// ─── Submit Button ────────────────────────────────────────────────────────────

@Composable
private fun SubmitButton(
    enabled : Boolean,
    onClick : () -> Unit
) {
    val buttonColor by animateColorAsState(
        targetValue   = if (enabled) YellowPrimary else YellowDim,
        animationSpec = tween(300),
        label         = "buttonColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Button(
            onClick  = onClick,
            enabled  = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape    = RoundedCornerShape(14.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = buttonColor,
                disabledContainerColor = YellowDim,
                contentColor           = Color.Black,
                disabledContentColor   = Color(0xFF5A4D00)
            )
        ) {
            Text(
                text       = "Submit Review",
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold,
                color      = if (enabled) Color.Black else Color(0xFF5A4D00)
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

/**
 * State 1: Default — no rating, empty text field, button dimmed.
 */
@Preview(
    name       = "State 1 – Default (no interaction)",
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
fun PreviewWriteReview_Default() {
    MaterialTheme {
        WriteReviewScreen()
    }
}

/**
 * State 2: Active — 4 stars selected, text entered, button bright yellow.
 * To preview this state, temporarily set initial values:
 *   rating = 4, reviewText = "abc"
 */
@Preview(
    name       = "State 2 – Active (4 stars + text)",
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
fun PreviewWriteReview_Active() {
    // Stateful wrapper to pre-fill for preview
    var rating     by remember { mutableStateOf(4) }
    var reviewText by remember { mutableStateOf("abc") }

    MaterialTheme {
        Scaffold(
            containerColor = BackgroundDark,
            topBar = { WriteReviewTopBar(onBack = {}) },
            bottomBar = { SubmitButton(enabled = rating > 0, onClick = {}) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))
                Text("How was your experience?", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("at King Barber Shop", color = TextSecondary, fontSize = 13.sp)
                Spacer(Modifier.height(28.dp))
                StarRatingRow(rating = rating, onRating = { rating = it })
                Spacer(Modifier.height(32.dp))
                Text("Your Review", color = TextSecondary, fontSize = 13.sp, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
                ReviewTextField(value = reviewText, onValueChange = { reviewText = it }, isActive = true)
            }
        }
    }
}