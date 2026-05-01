package com.example.barberapp.View.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.screenUI.customer.MenuItem
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.LogoutRed
import com.example.barberapp.View.utils.SurfaceDark
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary

class MenuItem {
}

// ── Menu item row ─────────────────────────────────────────────────────────────
@Composable
fun MenuItemRow(item: MenuItem) {
    val contentColor = if (item.isDestructive) LogoutRed else TextPrimary

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .clickable { item.onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = if (item.isDestructive) LogoutRed else GoldAccent,
            modifier = Modifier.Companion.size(22.dp)
        )

        Spacer(modifier = Modifier.Companion.width(14.dp))

        Text(
            text = item.label,
            color = contentColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Companion.Medium,
            modifier = Modifier.Companion.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.Companion.size(20.dp)
        )
    }
}