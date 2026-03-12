package com.example.findmycar.cardetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.data.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CarDetailsUiState {
    object Loading : CarDetailsUiState()
    data class Success(val car: Car) : CarDetailsUiState()
    data class Error(val message: String) : CarDetailsUiState()
}

class CarDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CarDetailsUiState>(CarDetailsUiState.Loading)
    val uiState: StateFlow<CarDetailsUiState> = _uiState.asStateFlow()

    fun loadCarDetails(carId: Int) {
        viewModelScope.launch {
            _uiState.value = CarDetailsUiState.Loading
            try {
                // TODO: Fetch from Supabase
                // For now, using mock data
                val mockCar = Car(
                    id = carId,
                    make = "Toyota",
                    model = "Camry",
                    year = 2024,
                    price = 28000.0,
                    mileage = 500,
                    fuelType = "Gasoline",
                    transmission = "Automatic",
                    engineSize = "2.5L 4-Cyl",
                    horsepower = 203,
                    color = "Celestial Silver Metallic",
                    description = "A reliable and fuel-efficient sedan with modern features and a comfortable ride. Perfect for daily commuting and long road trips.",
                    imageUrl = ""
                )
                _uiState.value = CarDetailsUiState.Success(mockCar)
            } catch (e: Exception) {
                _uiState.value = CarDetailsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
