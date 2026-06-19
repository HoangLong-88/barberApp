package com.example.barberapp.View.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.barberapp.View.screenUI.customer.home.GoldPrimary

class StarRow {
}

@Composable
fun StarRow(rating: Float, maxStars: Int = 5) {
    Row {
        repeat(maxStars) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.Companion.size(14.dp)
            )
        }
    }
}