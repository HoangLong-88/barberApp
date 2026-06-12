package com.example.barberapp.View.utils

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Simple diagonal stripes drawn with Canvas
@Composable
fun DiagonalStripes(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stripeWidth = 6.dp.toPx()
        val gap         = 6.dp.toPx()
        val step        = stripeWidth + gap
        var x           = -size.height
        while (x < size.width + size.height) {
            drawRect(
                color    = GoldAccent.copy(alpha = 0.25f),
                topLeft  = androidx.compose.ui.geometry.Offset(x, 0f),
                size     = androidx.compose.ui.geometry.Size(stripeWidth, size.height * 2)
            )
            x += step
        }
    }
}