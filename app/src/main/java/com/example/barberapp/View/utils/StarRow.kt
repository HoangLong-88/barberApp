package com.example.barberapp.View.utils

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

// ─── Star Rating Row ──────────────────────────────────────────────────────────

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
@Composable
fun StarRow(rating: Float, maxStars: Int = 5) {
    Row {
        repeat(maxStars) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.Companion.size(14.dp)
            )
        }
    }
}
@Composable
fun StarRatingRow(
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