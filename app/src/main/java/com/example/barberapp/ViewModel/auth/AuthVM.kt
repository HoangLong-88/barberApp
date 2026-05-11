package com.example.barberapp.ViewModel.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.barberapp.Repository.auth.AuthRepository
import com.example.barberapp.ViewModel.customer.UserVM

class AuthVM : ViewModel() {
    private val authRepo = AuthRepository();
    var uiState by mutableStateOf(AuthUIState())
        private set

    fun login(email: String, password: String, navController: NavController) {
        uiState = uiState.copy(isLoading = true, error = null)
        authRepo.checkLogin(email, password) { success, errorMessage ->
            uiState = if (success) {
                navController.navigate("main_graph") {
                    popUpTo("auth_graph") {
                        inclusive = true // Destroys the auth_graph and everything in it
                    }
                }
                uiState.copy(isLoading = false, loginSuccess = true)
            } else {
                uiState.copy(isLoading = false, error = errorMessage)
            }
        }
    }

    fun signUp(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String,
        profileImgUrl: String?,
        navController: NavController
        ) {
        uiState = uiState.copy(isLoading = true, error = null)
        authRepo.checkSignUp(name, email, phone, password, role, profileImgUrl)
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

    fun logOut(navController: NavController, userVM: UserVM) {
        authRepo.getLogOut()
        userVM.clearData()
        navController.navigate("auth_graph") {
            popUpTo("main_graph") {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    fun resetState() {  // ← gọi sau khi navigate
        uiState = AuthUIState()
    }
}

data class AuthUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false
)