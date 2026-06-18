package com.example.barberapp.View.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.screenUI.customer.home.GoldPrimary

@Composable
 fun WriteReviewButton(onClick: () -> Unit) {
    Spacer(Modifier.Companion.height(8.dp))
    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Companion.Center
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary),
            border = ButtonDefaults.outlinedButtonBorder.copy()
        ) {
            Text(
                text = "Write a Review",
                color = GoldPrimary,
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}