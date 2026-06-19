package com.example.barberapp.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.barberapp.Repository.AuthRepository

class AuthVM : ViewModel() {
    private val authRepo = AuthRepository();
    var uiState by mutableStateOf(AuthUIState())
        private set

    fun login(email: String, password: String,userVM: UserVM) {
        uiState = uiState.copy(isLoading = true, error = null)
        authRepo.checkLogin(email, password) { success, errorMessage, role ->
            uiState = (if (success) {
                userVM.fetchUserProfile()
                uiState.copy(isLoading = false, loginSuccess = true)
            } else {
                uiState.copy(isLoading = false, error = errorMessage)
            })
        }
    }

    fun signUp(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String,
        avatarUrl: String?,
        shopId: String,
        roleColorHex: String,
        navController: NavController
        ) {
        uiState = uiState.copy(isLoading = true, error = null)
        authRepo.checkSignUp(name, email, phone, password, role, avatarUrl,shopId, roleColorHex)
        { success, errorMessage ->
            uiState = if (success) {
                navController.navigate("login"){
                    popUpTo("register") { inclusive = true }
                }
                uiState.copy(isLoading = false, registerSuccess = true)
            } else {
                uiState.copy(isLoading = false, error = errorMessage)
            }
        }
    }

    fun logOut(userVM: UserVM,shopVM: ShopVM) {
        authRepo.getLogOut()
        userVM.clearData()
        shopVM.resetData()
        uiState = AuthUIState()
    }
    fun resetState() {  // ← gọi sau khi navigate
        uiState = AuthUIState()
    }
}

data class AuthUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false,
    val roleChecking: String = ""
)