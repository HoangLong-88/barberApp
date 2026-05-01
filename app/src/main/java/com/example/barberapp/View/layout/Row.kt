package com.example.barberapp.View.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.screenUI.customer.StatItem
import com.example.barberapp.View.component.StatCard
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary
import kotlin.collections.forEach

// ── User info ────────────────────────────────────────────────────────────────
@Composable
fun UserInfoRow(name: String, email: String, phone: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Avatar circle
        Box(
            modifier        = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(GoldAccent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Filled.Person,
                contentDescription = "Avatar",
                tint               = BackgroundDark,
                modifier           = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text       = name,
                color      = TextPrimary,
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = email, color = TextSecondary, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = phone, color = TextSecondary, fontSize = 13.sp)
        }
    }
}
// ── Stats row ─────────────────────────────────────────────────────────────────
@Composable
fun StatsRow(stats: List<StatItem>) {
    Row(
        modifier            = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stats.forEach { stat ->
            StatCard(stat = stat, modifier = Modifier.weight(1f))
        }
    }
}
