package com.example.barberapp.View.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.utils.GoldPrimary
import com.example.barberapp.View.utils.InputBorder
import com.example.barberapp.View.utils.SurfaceColor
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary

class TextField {
}

// ── Reusable labeled input field ───────────────────────────────────────────────
@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Companion.Text,
    modifier: Modifier = Modifier.Companion
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Companion.Normal,
            modifier = Modifier.Companion.padding(bottom = 6.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SurfaceColor,
                unfocusedContainerColor = SurfaceColor,
                disabledContainerColor = SurfaceColor,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = GoldPrimary,
                focusedIndicatorColor = Color.Companion.Transparent,
                unfocusedIndicatorColor = Color.Companion.Transparent,
                disabledIndicatorColor = Color.Companion.Transparent,
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.Companion
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = InputBorder,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
        )
    }
}

@Composable
fun DialogTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        TextField(
            value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF2C2C2C), unfocusedContainerColor = Color(0xFF2C2C2C), focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
            shape = RoundedCornerShape(12.dp), placeholder = { Text(placeholder, color = Color.Gray.copy(0.5f)) }, singleLine = true
        )
    }
}