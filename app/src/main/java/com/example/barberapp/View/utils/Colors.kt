package com.example.barberapp.View.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.barberapp.Model.types.NotifType

// ─── Color Palette ───────────────────────────────────────────────────────────
public val BackgroundDark = Color(0xFF0F0F0F)
public val SurfaceDark = Color(0xFF1C1C1C)
public val InputDark = Color(0xFF252525)
public val GoldPrimary = Color(0xFFD4A843)
public val GoldLight = Color(0xFFE8C060)
public val GoldDark = Color(0xFFB8902E)
public val TextPrimary = Color(0xFFFFFFFF)
public val TextSecondary = Color(0xFF9E9E9E)
public val TextHint = Color(0xFF5A5A5A)
public val BorderColor = Color(0xFF2E2E2E)
public val GoogleBtnBg = Color(0xFF1A1A1A)

public val BackgroundColor = Color(0xFF121212)

public val SurfaceColor = Color(0xFF1E1E1E)

public val PrimaryYellow = Color(0xFFFFD600)

// ─── Booking ────────────────────────────────────────────────────────────
public val FilterInactive = Color(0xFF333333)
public val BottomNavBg = Color(0xFF1F1F1F)
public val CardBg = Color(0xFF2A2A2A)
public val SearchBg = Color(0xFF2C2C2C)


// ───  Colors ────────────────────────────────────────────────────────────
public val CompletedBg = Color(0xFF1B4332)
public val CompletedText = Color(0xFF52B788)
public val PendingBg = Color(0xFF3B2F00)
public val PendingText = Color(0xFFFFD600)
public val CancelledBg = Color(0xFF3B0A0A)
public val CancelledText = Color(0xFFE53935)


// ─── AppNotification type tints ─────────────────────────────────────────────────

public val ConfirmedIconBg = Color(0xFF1B4332)
public val ConfirmedIconFg = Color(0xFF52B788)
public val ReminderIconBg = Color(0xFF3B2F00)
public val ReminderIconFg = Color(0xFFFFD600)
public val OfferIconBg = Color(0xFF3B2F00)
public val OfferIconFg = Color(0xFFFFD600)
public val CancelledIconBg = Color(0xFF3B0A0A)
public val CancelledIconFg = Color(0xFFE53935)

// ─── Profile type tints ─────────────────────────────────────────────────
public val GoldAccent = Color(0xFFF5A623)
public val LogoutRed = Color(0xFFE53935)

// ─── TextField Colors ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun barbershopTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = BorderColor,
    focusedContainerColor = InputDark,
    unfocusedContainerColor = InputDark,
    cursorColor = GoldPrimary,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = TextSecondary,
)
// ─── Helpers ─────────────────────────────────────────────────────────────────

fun notifColors(type: NotifType): Pair<Color, Color> = when (type) {
    NotifType.Confirmed -> ConfirmedIconBg to ConfirmedIconFg
    NotifType.Reminder -> ReminderIconBg to ReminderIconFg
    NotifType.Offer -> OfferIconBg to OfferIconFg
    NotifType.Cancelled -> CancelledIconBg to CancelledIconFg
}