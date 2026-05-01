package com.example.barberapp.View.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.SurfaceDark
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary

class Header {
}

// ── Top header ────────────────────────────────────────────────────────────────
@Composable
fun TopHeader(userName: String) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Column(modifier = Modifier.Companion.weight(1f)) {
            Text(
                text = "Good evening",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                Text(
                    text = "Find your barber ",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(text = "✂\uFE0F", fontSize = 18.sp)
            }
        }

        // Avatar circle
        Box(
            modifier = Modifier.Companion
                .size(42.dp)
                .clip(CircleShape)
                .background(SurfaceDark)
                .border(1.5.dp, GoldAccent.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Companion.Center
        ) {
            Text(
                text = userName,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Companion.Bold
            )
        }
    }
}

// ── Nearby header ─────────────────────────────────────────────────────────────
@Composable
fun NearbyHeader(modifier: Modifier = Modifier) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text       = "Nearby Barbers",
            color      = TextPrimary,
            fontSize   = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text       = "Favorites",
            color      = GoldAccent,
            fontSize   = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}