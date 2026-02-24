package com.example.findmycar.login

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val navigateToNextScreen: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun performLogin(enteredEmail: Editable?, enteredPassword: Editable?) {
        val emailString = enteredEmail?.toString()?.trim()
        val passwordString = enteredPassword?.toString()

        if (emailString.isNullOrBlank() || passwordString.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter both email and password.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

        viewModelScope.launch {
            try {
                supabase.auth.signInWith(Email) {
                    email = emailString
                    password = passwordString
                }
                
                Log.d("LoginViewModel", "Login successful")
                _uiState.update { 
                    it.copy(isLoading = false, navigateToNextScreen = true) 
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login failed", e)
                _uiState.update {
                    Log.e("LoginViewModel", "Login failed", e)

                    // Give useful error message to user when login fails
                    val msg = when {
                        e is SecurityException -> "Permission denied: Missing INTERNET permission in Manifest."
                        e.message?.contains("Unable to resolve host") == true -> "No internet connection."
                        else -> e.message ?: "Login failed. Please try again."
                    }

                    it.copy(isLoading = false, msg)
                }
            }
        }
    }

    fun performSignUp(enteredEmail: Editable?, enteredPassword: Editable?) {
        val emailString = enteredEmail?.toString()?.trim()
        val passwordString = enteredPassword?.toString()

        if (emailString.isNullOrBlank() || passwordString.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter email and password to create an account.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Attempting to sign up with email: $emailString")
                supabase.auth.signUpWith(
                    provider = Email,
                    redirectUrl = "findmycar://login-callback"
                ) {
                    email = emailString
                    password = passwordString
                }
                
                Log.d("LoginViewModel", "Sign up successful")
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        successMessage = "Account created! Please check your email for confirmation."
                    ) 
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Sign up failed", e)
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = e.message ?: "Sign up failed. Please try again.") 
                }
            }
        }
    }

    fun onNavigationComplete() {
        _uiState.update { it.copy(navigateToNextScreen = false) }
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    fun onSuccessMessageShown() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
