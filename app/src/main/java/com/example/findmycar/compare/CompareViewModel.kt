package com.example.findmycar.compare

import androidx.lifecycle.ViewModel
import com.example.findmycar.data.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class CompareUiState {
    object Loading : CompareUiState()
    data class Ready(val car1: Car, val car2: Car) : CompareUiState()
    data class Error(val message: String) : CompareUiState()
}

class CompareViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CompareUiState>(CompareUiState.Loading)
    val uiState: StateFlow<CompareUiState> = _uiState.asStateFlow()

    fun setCars(car1: Car, car2: Car) {
        _uiState.value = CompareUiState.Ready(car1, car2)
    }

    fun clearComparison() {
        _uiState.value = CompareUiState.Loading
    }
}
