package com.example.barberapp.View.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.utils.TextSecondary

// ─── Field Label ──────────────────────────────────────────────────────────────
@Composable
fun FieldLabel(text: String, fontSize: Int, fontWeight: FontWeight) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        modifier = Modifier.fillMaxWidth()
    )
}