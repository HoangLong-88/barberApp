package com.example.barberapp.View.state.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun checkAuthUiState(isSuccess: Boolean, onSuccess: () -> Unit) {
    LaunchedEffect(isSuccess) {
        if (isSuccess) onSuccess()
    }
}

