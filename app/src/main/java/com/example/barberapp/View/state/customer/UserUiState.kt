package com.example.barberapp.View.state.customer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.barberapp.Model.entities.User
import com.example.barberapp.ViewModel.customer.UserVM

@Composable
fun reloadCustomerInfoState(userInfo: User?, userVM: UserVM){
    if (userInfo == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        LaunchedEffect(Unit) { userVM.fetchUserProfile() }
        return
    }
}