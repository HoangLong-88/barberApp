package com.example.barberapp.ViewModel.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.example.barberapp.Repository.AuthRepository

class AuthVM: ViewModel() {
    private val authRepo = AuthRepository();
    var uiState by mutableStateOf(AuthUIState())
        private set
    fun login(email: String, password: String){
        uiState = uiState.copy(isLoading = true, error = null)
        authRepo.checkLogin(email,password){success, errorMessage ->
            uiState = if (success){
                uiState.copy(isLoading = false, isSuccess = true)
            } else {
                uiState.copy(isLoading = false, error =  errorMessage)
            }
        }
    }
    fun signUp(username: String, email: String, phone: String, password: String){
        uiState = uiState.copy()
        uiState = uiState.copy(isLoading = true, error = null)
        authRepo.checkSignUp(username,email,phone,password)
        {success, errorMessage ->
            uiState = if (success){
                uiState.copy(isLoading = false, isSuccess = true)
            } else {
                uiState.copy(isLoading = false, error =  errorMessage)
            }
        }
    }
}
data class AuthUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)