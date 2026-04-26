package com.example.barberapp.View.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.barberapp.View.UI.Customer.MainActivity
import com.example.barberapp.View.utils.BackgroundDark
import com.example.barberapp.View.utils.BorderColor
import com.example.barberapp.View.utils.GoldDark
import com.example.barberapp.View.utils.GoldLight
import com.example.barberapp.View.utils.GoldPrimary
import com.example.barberapp.View.utils.GoogleBtnBg
import com.example.barberapp.View.utils.InputDark
import com.example.barberapp.View.utils.SurfaceDark
import com.example.barberapp.View.utils.TextHint
import com.example.barberapp.View.utils.TextPrimary
import com.example.barberapp.View.utils.TextSecondary

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(
                onRegister = {
                    startActivity(Intent(this, SignUpActivity::class.java))
                },
                onLogin = { _,_,_ ->
                    startActivity(Intent(this, MainActivity::class.java))
                }
            )
        }
    }
}


// ─── Role Dropdown Item ───────────────────────────────────────────────────────
data class Role(val label: String, val value: String)

val roles = listOf(
    Role("Khách hàng", "customer"),
    Role("Nhân viên", "staff"),
    Role("Quản lý", "manager"),
)

// ─── Login Screen ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (email: String, password: String, role: String) -> Unit = { _, _, _ -> },
    onGoogleLogin: () -> Unit = {},
    onRegister: () -> Unit = {},
) {
    var selectedRole by remember { mutableStateOf(roles[0]) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Subtle gold shimmer animation on scissors icon
    val infiniteTransition = rememberInfiniteTransition(label = "gold_shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue  = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Subtle radial gold glow at top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            GoldPrimary.copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        radius = 600f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            // ── Scissors Icon ──────────────────────────────────────────────
            ScissorsIcon(alpha = shimmerAlpha)

            Spacer(Modifier.height(28.dp))

            // ── Title ──────────────────────────────────────────────────────
            Text(
                text = "Welcome Back",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Sign in to continue",
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(Modifier.height(40.dp))

            // ── Role Dropdown ──────────────────────────────────────────────
            FieldLabel("Vai trò")
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedRole.label,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    },
                    colors = barbershopTextFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    textStyle = LocalTextStyle.current.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier.background(SurfaceDark)
                ) {
                    roles.forEach { role ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    role.label,
                                    color = if (role == selectedRole) GoldPrimary else TextPrimary,
                                    fontWeight = if (role == selectedRole) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                selectedRole = role
                                dropdownExpanded = false
                            },
                            modifier = Modifier.background(SurfaceDark)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Email Field ────────────────────────────────────────────────
            FieldLabel("Email")
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("your@email.com", color = TextHint, fontSize = 14.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = barbershopTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(Modifier.height(20.dp))

            // ── Password Field ─────────────────────────────────────────────
            FieldLabel("Password")
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = TextHint, fontSize = 14.sp) },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = TextSecondary
                        )
                    }
                },
                colors = barbershopTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(Modifier.height(32.dp))

            // ── Login Button ───────────────────────────────────────────────
            Button(
                onClick = { onLogin(email, password, selectedRole.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(GoldDark, GoldPrimary, GoldLight)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Login",
                        color = Color(0xFF1A1000),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Google Login Button ────────────────────────────────────────
            OutlinedButton(
                onClick = onGoogleLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = GoogleBtnBg
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    GoogleLogo()
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Login with Google",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // ── Register Link ──────────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = "Register",
                    color = GoldPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onRegister() }
                )
            }

            Spacer(Modifier.height(32.dp))

            // Bottom indicator bar (like iOS home indicator)
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF3A3A3A))
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ─── Scissors SVG Icon (Canvas-drawn) ────────────────────────────────────────
@Composable
fun ScissorsIcon(alpha: Float = 1f) {
    // Using a Canvas to draw the scissors shape
    Canvas(
        modifier = Modifier.size(56.dp)
    ) {
        val w = size.width
        val h = size.height
        val goldColor = GoldPrimary.copy(alpha = alpha)

        // Left blade - upper arm
        drawLine(
            color = goldColor,
            start = Offset(w * 0.5f, h * 0.5f),
            end = Offset(w * 0.1f, h * 0.05f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Right blade - upper arm
        drawLine(
            color = goldColor,
            start = Offset(w * 0.5f, h * 0.5f),
            end = Offset(w * 0.9f, h * 0.05f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Left blade - lower arm
        drawLine(
            color = goldColor,
            start = Offset(w * 0.5f, h * 0.5f),
            end = Offset(w * 0.15f, h * 0.95f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Right blade - lower arm
        drawLine(
            color = goldColor,
            start = Offset(w * 0.5f, h * 0.5f),
            end = Offset(w * 0.85f, h * 0.95f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        // Center pivot circle
        drawCircle(
            color = goldColor,
            radius = 6f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
        // Left handle circle
        drawCircle(
            color = Color.Transparent,
            radius = 9f,
            center = Offset(w * 0.12f, h * 0.93f)
        )
        drawCircle(
            color = goldColor,
            radius = 9f,
            center = Offset(w * 0.12f, h * 0.93f),
            style = Stroke(width = 4f)
        )
        // Right handle circle
        drawCircle(
            color = goldColor,
            radius = 9f,
            center = Offset(w * 0.88f, h * 0.93f),
            style = Stroke(width = 4f)
        )
    }
}

// ─── Google Logo (Colored circles) ───────────────────────────────────────────
@Composable
fun GoogleLogo() {
    Box(
        modifier = Modifier.size(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "G",
            color = Color(0xFF4285F4),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ─── Field Label ──────────────────────────────────────────────────────────────
@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.fillMaxWidth()
    )
}

// ─── TextField Colors ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun barbershopTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor    = GoldPrimary,
    unfocusedBorderColor  = BorderColor,
    focusedContainerColor = InputDark,
    unfocusedContainerColor = InputDark,
    cursorColor           = GoldPrimary,
    focusedLabelColor     = GoldPrimary,
    unfocusedLabelColor   = TextSecondary,
)

// ─── Preview ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F, widthDp = 375, heightDp = 812)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}