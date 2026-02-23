package com.example.findmycar.templates

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A data class to represent the state of the UI.
 * It should contain all the information the UI needs to render itself.
 *
 * @param isLoading True if data is being loaded, false otherwise.
 * @param someData The data to be displayed on the screen.
 * @param errorMessage An optional error message to display.
 */
@Suppress("unused")
data class TemplateUiState(
    val isLoading: Boolean = false,
    val someData: String? = null,
    val errorMessage: String? = null
)

/**
 * TemplateViewModel manages the UI state for the corresponding screen (Fragment/Activity).
 * It survives configuration changes and holds the application's UI data.
 */
@Suppress("unused")
class TemplateViewModel : ViewModel() {

    // _uiState is the private mutable state that can only be modified from within the ViewModel.
    // It is initialized with a default state.
    private val _uiState = MutableStateFlow(TemplateUiState())

    // uiState is the public, read-only state that the UI can observe.
    // This ensures that the UI cannot directly modify the state.
    val uiState: StateFlow<TemplateUiState> = _uiState.asStateFlow()

    /**
     * Example function to demonstrate how to update the UI state.
     * This function might be called when a user performs an action, like clicking a button.
     */
    fun doSomething() {
        // Use the update function to make atomic and thread-safe state updates.
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }

        // Here you would typically perform a long-running operation,
        // like making a network request or querying a database.

        // After the operation is complete, update the state again with the result.
        // For this example, we'll just simulate a successful result.
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                someData = "Here is some data from the ViewModel!"
            )
        }
    }

    /**
     * Example function to handle an error scenario.
     */
    fun produceError() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                errorMessage = "Something went wrong!"
            )
        }
    }

    /**
     * Function to be called by the UI when the error has been shown and should be cleared.
     */
    fun errorShown() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}
