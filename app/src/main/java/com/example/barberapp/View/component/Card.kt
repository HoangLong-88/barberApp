package com.example.barberapp.View.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.Model.entities.Booking
import com.example.barberapp.Model.entities.Employee
import com.example.barberapp.Model.entities.Notification
import com.example.barberapp.Model.entities.Shop
import com.example.barberapp.View.screenUI.customer.bookings.BookingStatus
import com.example.barberapp.View.screenUI.customer.bookings.FilterTab
import com.example.barberapp.View.screenUI.customer.home.AvatarBg
import com.example.barberapp.View.screenUI.customer.home.CardDark
import com.example.barberapp.View.screenUI.customer.profile.StatItem
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.CancelledBg
import com.example.barberapp.View.utils.CancelledText
import com.example.barberapp.View.utils.CardBg
import com.example.barberapp.View.utils.CompletedBg
import com.example.barberapp.View.utils.CompletedText
import com.example.barberapp.View.utils.GoldAccent
import com.example.barberapp.View.utils.GoldPrimary
import com.example.barberapp.View.utils.LogoutRed
import com.example.barberapp.View.utils.PendingBg
import com.example.barberapp.View.utils.PendingText
import com.example.barberapp.View.utils.PrimaryYellow
import com.example.barberapp.View.utils.SurfaceColor
import com.example.barberapp.View.utils.SurfaceDark
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary
import com.example.barberapp.View.utils.notifColors

class Card {
}

@Composable
fun StatCard(stat: StatItem, modifier: Modifier = Modifier.Companion) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text(
            text = stat.value,
            color = GoldAccent,
            fontSize = 22.sp,
            fontWeight = FontWeight.Companion.Bold
        )
        Spacer(modifier = Modifier.Companion.height(4.dp))
        Text(
            text = stat.label,
            color = TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Companion.Center
        )
    }
}

// ─── Booking Card ─────────────────────────────────────────────────────────────
@Composable
fun BookingCard(booking: Booking) {
    val (statusBg, statusFg) = when (booking.status) {
        BookingStatus.Completed -> CompletedBg to CompletedText
        BookingStatus.Pending   -> PendingBg to PendingText
        BookingStatus.Cancelled -> CancelledBg to CancelledText
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        modifier = Modifier.Companion.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = booking.service,
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(
                    label = booking.status.name,
                    background = statusBg,
                    textColor = statusFg
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(booking.shop, color = TextSecondary, fontSize = 13.sp)
            Text(booking.dateTime, color = TextSecondary, fontSize = 13.sp)
            Text("Barber: ${booking.barber}", color = TextSecondary, fontSize = 13.sp)

            Spacer(Modifier.height(10.dp))

            Text(
                text = booking.price,
                color = statusFg,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
// ─── Filter Tab Row ───────────────────────────────────────────────────────────
@Composable
fun FilterTabRow(
    selected: FilterTab,
    onSelect: (FilterTab) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        FilterTab.entries.forEach { tab ->
            FilterChip(
                label = tab.name,
                isSelected = selected == tab,
                onClick = { onSelect(tab) }
            )
        }
    }
}

// ── Barber shop card ──────────────────────────────────────────────────────────
@Composable
fun BarberShopCard(
    shop: Shop?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var isFav by remember { mutableStateOf(shop?.isFavorite) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .clickable(onClick = onClick)
    ) {
        // Image placeholder with gradient overlay + heart button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.Companion.verticalGradient(
                        colors = listOf(Color(0xFF3A2800), Color(0xFF1A1200))
                    )
                )
        ) {
            // ── Replace Box below with AsyncImage / Image(painterResource(...)) ──
            Box(
                modifier = Modifier.Companion.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📸  Shop Image",
                    color = TextSecondary.copy(alpha = 0.4f),
                    fontSize = 14.sp
                )
            }

            // Gradient scrim at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.Companion.verticalGradient(
                            colors = listOf(Color.Companion.Transparent, CardBg)
                        )
                    )
            )

            // Heart icon
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(BackgroundDark.copy(alpha = 0.5f))
                    .align(Alignment.TopEnd)
                    .clickable { isFav = !isFav!! },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFav == true) Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFav == true) LogoutRed else TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Info section
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = shop?.name ?: "Loading...",
                    color      = TextPrimary,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(3.dp))
                    Text(
                        text       = shop?.rating.toString(),
                        color      = TextPrimary,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text     = shop?.address ?: "Loading...",
                    color    = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Companion.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = shop?.priceRange ?: "Loading...",
                    color    = TextSecondary,
                    fontSize = 13.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.clickable { }
                ) {
                    Text(
                        text       = "Book Now",
                        color      = GoldAccent,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "→", color = GoldAccent, fontSize = 13.sp)
                }
            }
        }
    }
}

// ─── Notification Card ────────────────────────────────────────────────────────
@Composable
fun NotificationCard(
    notification: Notification,
    onDelete: () -> Unit
) {
    val (iconBg, iconFg) = notifColors(notification.type)
    val icon = notifIcon(notification.type)

    // Animate card background: slightly brighter when unread
    val cardBg by animateColorAsState(
        targetValue = if (notification.isUnread) Color(0xFF262626) else SurfaceColor,
        animationSpec = tween(300),
        label = "cardBg"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bubble
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = notification.type.name,
                    tint = iconFg,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = notification.body,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.timeAgo,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    if (notification.isUnread) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(PrimaryYellow)
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // Delete button
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete notification",
                tint = TextSecondary,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDelete() }
                    .align(Alignment.Top)
            )
        }
    }
}

@Composable
fun BarberCard(barber: Employee?) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors    = CardDefaults.cardColors(containerColor = CardDark),
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar — initial letter in a circle
            Box(
                modifier         = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AvatarBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = barber?.name?:"Loading...".first().toString(),
                    color      = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp
                )
            }

            Spacer(Modifier.width(14.dp))

            // Name + specialties
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = barber?.name?: "Loading...",
                    color      = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 15.sp
                )
                Spacer(Modifier.height(2.dp))
            }

            // Star + numeric rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = Icons.Default.Star,
                    contentDescription = null,
                    tint               = GoldPrimary,
                    modifier           = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text       = barber?.rating?.toString()?:"Loading...",
                    color      = GoldPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp
                )
            }
        }
    }
}