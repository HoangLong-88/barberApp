package com.example.barberapp.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

import com.example.barberapp.utilities.BackgroundDark
import com.example.barberapp.utilities.TextPrimary
import com.example.barberapp.utilities.TextSecondary
import com.example.barberapp.utilities.TextHint
import com.example.barberapp.utilities.GoldDark
import com.example.barberapp.utilities.GoldLight
import com.example.barberapp.utilities.GoldPrimary
import com.example.barberapp.utilities.BorderColor
import com.example.barberapp.utilities.GoogleBtnBg
import com.example.barberapp.utilities.SurfaceDark
import com.example.barberapp.utilities.InputDark



class SignUpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreen()
        }
    }
}

// ─── Register Screen ──────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit = {},
    onCreateAccount: (
        fullName: String,
        email: String,
        phone: String,
        password: String
    ) -> Unit = { _, _, _, _ -> },
    onLogin: () -> Unit = {},
) {
    var fullName         by remember { mutableStateOf("") }
    var email            by remember { mutableStateOf("") }
    var phone            by remember { mutableStateOf("") }
    var password         by remember { mutableStateOf("") }
    var confirmPassword  by remember { mutableStateOf("") }
    var passwordVisible  by remember { mutableStateOf(false) }
    var confirmVisible   by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
        ) {
            Spacer(Modifier.height(52.dp))

            // ── Back Button ────────────────────────────────────────────────
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(36.dp)
                    .offset(x = (-8).dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Title ──────────────────────────────────────────────────────
            Text(
                text = "Create Account",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Sign up to get started",
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(Modifier.height(36.dp))

            // ── Full Name ──────────────────────────────────────────────────
            RegisterFieldLabel("Full Name")
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("John Doe", color = TextHint, fontSize = 14.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = registerTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(Modifier.height(18.dp))

            // ── Email ──────────────────────────────────────────────────────
            RegisterFieldLabel("Email")
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("your@email.com", color = TextHint, fontSize = 14.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = registerTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(Modifier.height(18.dp))

            // ── Phone Number ───────────────────────────────────────────────
            RegisterFieldLabel("Phone Number")
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = { Text("090xxxxxxx", color = TextHint, fontSize = 14.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = registerTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(Modifier.height(18.dp))

            // ── Password ───────────────────────────────────────────────────
            RegisterFieldLabel("Password")
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = TextHint, fontSize = 14.sp) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                },
                colors = registerTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )

            Spacer(Modifier.height(18.dp))

            // ── Confirm Password ───────────────────────────────────────────
            RegisterFieldLabel("Confirm Password")
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("••••••••", color = TextHint, fontSize = 14.sp) },
                singleLine = true,
                visualTransformation = if (confirmVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(
                            imageVector = if (confirmVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                },
                colors = registerTextFieldColors(
                    // Highlight red border if passwords don't match
                    focusedBorder = if (confirmPassword.isNotEmpty() && confirmPassword != password)
                        Color(0xFFE53935) else GoldPrimary,
                    unfocusedBorder = if (confirmPassword.isNotEmpty() && confirmPassword != password)
                        Color(0xFFE53935).copy(alpha = 0.6f) else BorderColor
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )

            // Passwords don't match warning
            if (confirmPassword.isNotEmpty() && confirmPassword != password) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Passwords do not match",
                    color = Color(0xFFE53935),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Create Account Button ──────────────────────────────────────
            val isFormValid = fullName.isNotBlank()
                    && email.isNotBlank()
                    && phone.isNotBlank()
                    && password.isNotBlank()
                    && password == confirmPassword

            Button(
                onClick = {
                    if (isFormValid) onCreateAccount(fullName, email, phone, password)
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = if (isFormValid)
                                Brush.horizontalGradient(listOf(GoldDark, GoldPrimary, GoldLight))
                            else
                                Brush.horizontalGradient(
                                    listOf(
                                        GoldDark.copy(alpha = 0.4f),
                                        GoldPrimary.copy(alpha = 0.4f),
                                        GoldLight.copy(alpha = 0.4f)
                                    )
                                ),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Create Account",
                        color = if (isFormValid) Color(0xFF1A1000) else Color(0xFF1A1000).copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // ── Login Link ─────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have account? ",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = "Login",
                    color = GoldPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLogin() }
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── Bottom indicator bar ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(120.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF3A3A3A))
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ─── Field Label ──────────────────────────────────────────────────────────────
@Composable
private fun RegisterFieldLabel(text: String) {
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
private fun registerTextFieldColors(
    focusedBorder: Color = GoldPrimary,
    unfocusedBorder: Color = BorderColor,
) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = focusedBorder,
    unfocusedBorderColor    = unfocusedBorder,
    focusedContainerColor   = InputDark,
    unfocusedContainerColor = InputDark,
    cursorColor             = GoldPrimary,
    focusedLabelColor       = GoldPrimary,
    unfocusedLabelColor     = TextSecondary,
    disabledContainerColor  = InputDark,
)

// ─── Preview ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F, widthDp = 375, heightDp = 812)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen()
    }
}


