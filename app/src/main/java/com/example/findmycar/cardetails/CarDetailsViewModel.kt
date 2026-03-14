package com.example.findmycar.cardetails

import androidx.lifecycle.ViewModel
import com.example.findmycar.data.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class CarDetailsUiState {
    object Loading : CarDetailsUiState()
    data class Success(val car: Car) : CarDetailsUiState()
    data class Error(val message: String) : CarDetailsUiState()
}

class CarDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CarDetailsUiState>(CarDetailsUiState.Loading)
    val uiState: StateFlow<CarDetailsUiState> = _uiState.asStateFlow()

    fun setCar(car: Car) {
        _uiState.value = CarDetailsUiState.Success(car)
    }
}
