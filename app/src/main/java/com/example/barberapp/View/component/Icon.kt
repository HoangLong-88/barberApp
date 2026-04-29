package com.example.barberapp.View.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.barberapp.View.utils.GoldPrimary

// ─── Scissors SVG Icon (Canvas-drawn) ────────────────────────────────────────
@Composable
fun ScissorsIcon(alpha: Float = 1f) {
    // Using a Canvas to draw the scissors shape
    Canvas(
        modifier = Modifier.size(56.dp)
    ) {
        val w = size.width
        val h = size.height
        val goldColor = GoldPrimary.copy(alpha = alpha)

        // Left blade - upper arm
        drawLine(
            color = goldColor,
            start = Offset(w * 0.5f, h * 0.5f),
            end = Offset(w * 0.1f, h * 0.05f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Right blade - upper arm
        drawLine(
            color = goldColor,
            start = Offset(w * 0.5f, h * 0.5f),
            end = Offset(w * 0.9f, h * 0.05f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Left blade - lower arm
        drawLine(
            color = goldColor,
            start = Offset(w * 0.5f, h * 0.5f),
            end = Offset(w * 0.15f, h * 0.95f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Right blade - lower arm
        drawLine(
            color = goldColor,
            start = Offset(w * 0.5f, h * 0.5f),
            end = Offset(w * 0.85f, h * 0.95f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Center pivot circle
        drawCircle(
            color = goldColor,
            radius = 6f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
        // Left handle circle
        drawCircle(
            color = Color.Transparent,
            radius = 9f,
            center = Offset(w * 0.12f, h * 0.93f)
        )
        drawCircle(
            color = goldColor,
            radius = 9f,
            center = Offset(w * 0.12f, h * 0.93f),
            style = Stroke(width = 4f)
        )
        // Right handle circle
        drawCircle(
            color = goldColor,
            radius = 9f,
            center = Offset(w * 0.88f, h * 0.93f),
            style = Stroke(width = 4f)
        )
    }
}
