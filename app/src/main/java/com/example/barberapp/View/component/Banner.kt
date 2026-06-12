package com.example.barberapp.View.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.DiagonalStripes
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.LogoutRed
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary

class Banner {
}

// ── Promo banner ──────────────────────────────────────────────────────────────
@Composable
 fun PromoBanner(modifier: Modifier = Modifier.Companion) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.Companion.horizontalGradient(
                    colors = listOf(Color(0xFF1A1A1A), Color(0xFF2A2000))
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.Companion.horizontalGradient(
                    listOf(GoldAccent.copy(alpha = 0.6f), Color.Companion.Transparent)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            )
    ) {
        // Diagonal stripes decoration (right side)
        DiagonalStripes(
            modifier = Modifier.Companion
                .align(Alignment.Companion.CenterEnd)
                .fillMaxHeight()
                .width(30.dp)
        )

        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = "BARBER SHOP",
                color = GoldAccent,
                fontSize = 20.sp,
                fontWeight = FontWeight.Companion.ExtraBold,
                letterSpacing = 2.sp
            )

            Column {
                Text(
                    text = "LIMITED OFFER",
                    color = LogoutRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    letterSpacing = 1.sp
                )
                Row(verticalAlignment = Alignment.Companion.Bottom) {
                    Column {
                        Text(
                            text = "Get 20% discount",
                            color = TextPrimary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "for first haircut",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                    Text(
                        text = "20%",
                        color = TextPrimary.copy(alpha = 0.15f),
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Companion.ExtraBold
                    )
                    Spacer(modifier = Modifier.Companion.width(4.dp))
                    Text(
                        text = "OFF",
                        color = GoldAccent,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Companion.ExtraBold
                    )
                }
            }

            // Gift Now button
            Box(
                modifier = Modifier.Companion
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                    .background(GoldAccent)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "GIFT NOW",
                    color = BackgroundDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}