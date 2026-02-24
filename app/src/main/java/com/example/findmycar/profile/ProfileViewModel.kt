package com.example.findmycar.profile

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.findmycar.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val isLoading: Boolean = false,
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun saveProfile(fullName: Editable?) {
        Log.d("ProfileViewModel", "saveProfile() called with fullName: $fullName")

        // This will talk to supabase to save the user's profile in a data store. For now log it
        Log.d("ProfileViewModel", "Saving profile to data store...")

    }

}