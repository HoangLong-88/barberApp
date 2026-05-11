package com.example.barberapp.View.service.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.barberapp.View.utils.GoldPrimary
import com.example.barberapp.ViewModel.auth.AuthVM

@Composable
fun onAuthUIService(isSuccess: Boolean, onSuccess: () -> Unit) {
    LaunchedEffect(isSuccess) {
        if (isSuccess) onSuccess()
    }
}

