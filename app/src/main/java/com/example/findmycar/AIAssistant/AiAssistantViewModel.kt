package com.example.findmycar.aiassistant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.data.AiMessage
import com.example.findmycar.data.AiRequest
import com.example.findmycar.data.AiResponse
import com.example.findmycar.data.MarketcheckListing
import com.example.findmycar.data.MarketcheckService
import com.example.findmycar.data.supabase
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val carListings: List<MarketcheckListing>? = null
)

data class AiAssistantUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AiAssistantViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AiAssistantUiState())
    val uiState: StateFlow<AiAssistantUiState> = _uiState.asStateFlow()
    
    private val marketcheckService = MarketcheckService()
    private val json = Json { ignoreUnknownKeys = true }

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
                // Convert UI messages to history format
                val history = _uiState.value.messages.map {
                    AiMessage(role = if (it.isUser) "user" else "assistant", content = it.content)
                }

                val httpResponse = supabase.functions.invoke(
                    function = "openai-chat",
                    body = AiRequest(messages = history, mode = "general"),
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )

                val response = httpResponse.body<AiResponse>()

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
