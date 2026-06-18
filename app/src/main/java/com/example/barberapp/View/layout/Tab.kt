package com.example.barberapp.View.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.screenUI.customer.home.DividerColor
import com.example.barberapp.View.screenUI.customer.home.GoldPrimary
import com.example.barberapp.Model.types.ShopDetailTab
import com.example.barberapp.View.screenUI.customer.home.TabInactive

@Composable
fun ShopDetailTabRow(
    selected: ShopDetailTab,
    onSelect: (ShopDetailTab) -> Unit
) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        ShopDetailTab.entries.forEach { tab ->
            val isActive = tab == selected
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                modifier = Modifier.Companion
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                TextButton(onClick = { onSelect(tab) }) {
                    Text(
                        text = tab.label,
                        color = if (isActive) GoldPrimary else TabInactive,
                        fontWeight = if (isActive) FontWeight.Companion.SemiBold else FontWeight.Companion.Normal,
                        fontSize = 14.sp
                    )
                }
                if (isActive) {
                    Box(
                        modifier = Modifier.Companion
                            .height(2.dp)
                            .fillMaxWidth(0.6f)
                            .background(GoldPrimary, RoundedCornerShape(1.dp))
                    )
                }
            }
        }
    }

    Divider(color = DividerColor, thickness = 1.dp)
}