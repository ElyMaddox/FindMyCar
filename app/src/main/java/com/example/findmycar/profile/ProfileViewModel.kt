package com.example.findmycar.profile

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Use isLoading for whenever fetching or updating data from/to supabase
data class ProfileUiState(
    val isLoading: Boolean = false,
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun saveProfile(fullName: Editable?) {
        Log.d("ProfileViewModel", "saveProfile() called with fullName: $fullName")

        viewModelScope.launch {
            try {
                // This will talk to supabase to save the user's profile in a data store. For now log it
                Log.d("ProfileViewModel", "Saving profile to data store...")
            }
            catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving profile", e)
            }
        }


    }

}