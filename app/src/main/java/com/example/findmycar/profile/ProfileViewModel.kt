package com.example.findmycar.profile

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.data.model.Profile
import com.example.findmycar.data.repository.ProfileRepository
import com.example.findmycar.data.supabase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val profile = repository.getProfile()
                _uiState.update { it.copy(isLoading = false, profile = profile) }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading profile", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun saveProfile(fullName: Editable?) {
        val name = fullName?.toString()?.trim()
        if (name.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your name.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = supabase.auth.currentUserOrNull()
                    ?: throw IllegalStateException("Not logged in")

                val existing = repository.getProfile()
                val profile = Profile(
                    id = existing?.id ?: user.id,
                    userId = user.id,
                    fullName = name,
                    email = user.email ?: "",
                    createdAt = existing?.createdAt ?: ""
                )
                repository.upsertProfile(profile)

                _uiState.update {
                    it.copy(isLoading = false, profile = profile, successMessage = "Profile saved!")
                }
                Log.d("ProfileViewModel", "Profile saved successfully")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving profile", e)
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onSuccessMessageShown() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
