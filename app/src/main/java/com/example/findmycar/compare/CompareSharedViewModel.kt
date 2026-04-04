package com.example.findmycar.compare

import androidx.lifecycle.ViewModel
import com.example.findmycar.data.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CompareSelectionState(
    val car1: Car? = null,
    val car2: Car? = null
) {
    val selectedCount: Int
        get() = listOfNotNull(car1, car2).size
}

class CompareSharedViewModel : ViewModel() {

    private val _selectionState = MutableStateFlow(CompareSelectionState())
    val selectionState: StateFlow<CompareSelectionState> = _selectionState.asStateFlow()

    fun addCar(car: Car): Int {
        val current = _selectionState.value
        return if (current.car1 == null) {
            _selectionState.update { it.copy(car1 = car) }
            1
        } else {
            _selectionState.update { it.copy(car2 = car) }
            2
        }
    }

    fun clearSelection() {
        _selectionState.update { CompareSelectionState() }
    }
}
