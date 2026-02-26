package com.example.findmycar.AIAssistant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class AiRequest(val message: String)

/**
 * These classes now match the complex structure returned by your Edge Function.
 */
@Serializable
data class AiResponse(
    val output: List<AiOutput> = emptyList()
)

@Serializable
data class AiOutput(
    val content: List<AiContent> = emptyList()
)

@Serializable
data class AiContent(
    val text: String = ""
)

data class AiAssistantUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AiAssistantViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AiAssistantUiState())
    val uiState: StateFlow<AiAssistantUiState> = _uiState.asStateFlow()

    fun sendUserMessage(messageText: String) {
        if (messageText.isBlank()) return

        val userMessage = ChatMessage(content = messageText, isUser = true)
        
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + userMessage,
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                Log.d("AiAssistantViewModel", "Sending message: $messageText")
                
                val httpResponse = supabase.functions.invoke(
                    function = "openai-chat",
                    body = AiRequest(message = messageText),
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )

                // Log the raw response for debugging
                val rawBody = httpResponse.bodyAsText()
                Log.d("AiAssistantViewModel", "Raw Response: $rawBody")

                // Decode using the new nested structure
                val response = httpResponse.body<AiResponse>()

                // Safely navigate the nested lists to find the response text
                val aiReplyText = response.output.firstOrNull()?.content?.firstOrNull()?.text 
                    ?: "AI returned an empty response."

                val aiMessage = ChatMessage(content = aiReplyText, isUser = false)
                _uiState.update { it.copy(messages = it.messages + aiMessage, isLoading = false) }

            } catch (e: Exception) {
                Log.e("AiAssistantViewModel", "Error getting AI response", e)
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to get AI response") }
            }
        }
    }
}
