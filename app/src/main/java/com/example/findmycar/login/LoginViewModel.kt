package com.example.findmycar.login

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val errorMessage: String? = null,
    val navigateToNextScreen: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun performLogin(username: Editable?, password: Editable?) {
        if (username == null || password == null) {
            Log.d(
                this::class.java.simpleName,
                "Login failed, entered no information in one or more fields"
            )
            _uiState.update { it.copy(errorMessage = "Please enter both username and password.") }
            return
        }

        // For now, let any login succeed. Will need to check against "database" later on.
        // val usernameString = username.toString()
        // val passwordString = password.toString()

        Log.d(this::class.java.simpleName, "Login successful")
        _uiState.update { it.copy(navigateToNextScreen = true) }
    }

    fun onNavigationComplete() {
        _uiState.update { it.copy(navigateToNextScreen = false) }
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
