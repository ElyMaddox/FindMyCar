package com.example.findmycar.AIAssistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AiAssistantViewModel : ViewModel() {

    fun sendUserMessage(message: String) {

        viewModelScope.launch {
            // Do message stuff
        }


    }
}