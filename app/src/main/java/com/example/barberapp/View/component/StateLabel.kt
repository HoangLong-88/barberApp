package com.example.barberapp.View.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.utils.BackgroundColor
import com.example.barberapp.View.utils.IconTint
import com.example.barberapp.View.utils.OnSurfaceVariant

class StateLabel {
}

@Composable
fun EmptyStateLabel(
    modifier: Modifier = Modifier.Companion,
    imageVector: ImageVector,
    primaryMessage: String,
    hintMessage: String = ""
) {
    Column(
        modifier = modifier
            .background(BackgroundColor)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Heart icon
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = IconTint,
            modifier = Modifier.Companion.size(90.dp)
        )

        Spacer(modifier = Modifier.Companion.height(16.dp))

        // Primary message
        Text(
            text = primaryMessage,
            color = OnSurfaceVariant,
            fontSize = 16.sp,
            fontWeight = FontWeight.Companion.Medium,
            textAlign = TextAlign.Companion.Center
        )

        Spacer(modifier = Modifier.Companion.height(6.dp))

        // Subtitle / hint
        Text(
            text = hintMessage,
            color = IconTint,
            fontSize = 13.sp,
            fontWeight = FontWeight.Companion.Normal,
            textAlign = TextAlign.Companion.Center,
            lineHeight = 18.sp
        )
    }
}