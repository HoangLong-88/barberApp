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
val BackgroundDark = Color(0xFF0F0F0F)
val SurfaceDark = Color(0xFF1C1C1C)
val InputDark = Color(0xFF252525)
val GoldPrimary = Color(0xFFD4A843)
val GoldLight = Color(0xFFE8C060)
val GoldDark = Color(0xFFB8902E)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF9E9E9E)
val TextHint = Color(0xFF5A5A5A)
val BorderColor = Color(0xFF2E2E2E)
val GoogleBtnBg = Color(0xFF1A1A1A)

val BackgroundColor = Color(0xFF121212)

val SurfaceColor = Color(0xFF1E1E1E)

val PrimaryYellow = Color(0xFFFFD600)

// ─── Booking ────────────────────────────────────────────────────────────
val FilterInactive = Color(0xFF333333)
val BottomNavBg = Color(0xFF1F1F1F)
val CardBg = Color(0xFF2A2A2A)
val SearchBg = Color(0xFF2C2C2C)


// ───  Colors ────────────────────────────────────────────────────────────
val CompletedBg = Color(0xFF1B4332)
val CompletedText = Color(0xFF52B788)
val PendingBg = Color(0xFF3B2F00)
val PendingText = Color(0xFFFFD600)
val CancelledBg = Color(0xFF3B0A0A)
val CancelledText = Color(0xFFE53935)


// ─── AppNotification type tints ─────────────────────────────────────────────────

val ConfirmedIconBg = Color(0xFF1B4332)
val ConfirmedIconFg = Color(0xFF52B788)
val ReminderIconBg = Color(0xFF3B2F00)
val ReminderIconFg = Color(0xFFFFD600)
val OfferIconBg = Color(0xFF3B2F00)
val OfferIconFg = Color(0xFFFFD600)
val CancelledIconBg = Color(0xFF3B0A0A)
val CancelledIconFg = Color(0xFFE53935)

// ─── Profile type tints ─────────────────────────────────────────────────
val GoldAccent = Color(0xFFF5A623)
val LogoutRed = Color(0xFFE53935)
val InputBorder = Color(0xFF2A2A2A)
// ─── Favorites type tints ─────────────────────────────────────────────────
val OnSurfaceVariant = Color(0xFF9E9E9E)
val IconTint         = Color(0xFF757575)
val OnSurface        = Color(0xFFFFFFFF)

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

// ─── TextField Colors ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun registerTextFieldColors(
    focusedBorder: Color = GoldPrimary,
    unfocusedBorder: Color = BorderColor,
) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = focusedBorder,
    unfocusedBorderColor = unfocusedBorder,
    focusedContainerColor = InputDark,
    unfocusedContainerColor = InputDark,
    cursorColor = GoldPrimary,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = TextSecondary,
    disabledContainerColor = InputDark,
)