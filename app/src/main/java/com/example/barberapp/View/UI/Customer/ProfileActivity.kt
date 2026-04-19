package com.example.barberapp.View.UI.Customer

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.barberapp.View.UI.component.SharedBottomNavBar
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.BottomNavBg
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.LogoutRed
import com.example.barberapp.View.utils.SurfaceDark
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary

// ── Data models ─────────────────────────────────────────────────────────────
data class StatItem(val value: String, val label: String)

data class MenuItem(
    val icon: ImageVector,
    val label: String,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit = {}
)

// ── Screen ───────────────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val stats = listOf(
        StatItem("12",  "Bookings"),
        StatItem("5",   "Reviews"),
        StatItem("320", "Points"),
    )

    val menuItems = listOf(
        MenuItem(Icons.Outlined.Edit,             "Edit Profile"),
        MenuItem(Icons.Outlined.DateRange,        "My Bookings"),
        MenuItem(Icons.Outlined.FavoriteBorder,   "Favorites"),
        MenuItem(Icons.Outlined.Notifications,    "Notifications"),
        MenuItem(Icons.AutoMirrored.Outlined.Logout, "Logout", isDestructive = true),
    )

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar     = { SharedBottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Title
            Text(
                text       = "Profile",
                color      = TextPrimary,
                fontSize   = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier   = Modifier.padding(bottom = 24.dp)
            )

            // ── User info row ──────────────────────────────────────────────
            UserInfoRow(
                name  = "John Doe",
                email = "john@email.com",
                phone = "090xxxxxxx"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Stats row ─────────────────────────────────────────────────
            StatsRow(stats = stats)

            Spacer(modifier = Modifier.height(20.dp))

            // ── Menu items ────────────────────────────────────────────────
            menuItems.forEachIndexed { index, item ->
                MenuItemRow(item = item)
                if (index < menuItems.lastIndex) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

// ── User info ────────────────────────────────────────────────────────────────
@Composable
private fun UserInfoRow(name: String, email: String, phone: String) {
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
private fun StatsRow(stats: List<StatItem>) {
    Row(
        modifier            = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stats.forEach { stat ->
            StatCard(stat = stat, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(stat: StatItem, modifier: Modifier = Modifier) {
    Column(
        modifier          = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text       = stat.value,
            color      = GoldAccent,
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text      = stat.label,
            color     = TextSecondary,
            fontSize  = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ── Menu item row ─────────────────────────────────────────────────────────────
@Composable
private fun MenuItemRow(item: MenuItem) {
    val contentColor = if (item.isDestructive) LogoutRed else TextPrimary

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .clickable { item.onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = item.icon,
            contentDescription = item.label,
            tint               = if (item.isDestructive) LogoutRed else GoldAccent,
            modifier           = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text       = item.label,
            color      = contentColor,
            fontSize   = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier   = Modifier.weight(1f)
        )

        Icon(
            imageVector        = Icons.Default.ChevronRight,
            contentDescription = null,
            tint               = TextSecondary,
            modifier           = Modifier.size(20.dp)
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF1C1C1C)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(navController = rememberNavController())
    }
}
