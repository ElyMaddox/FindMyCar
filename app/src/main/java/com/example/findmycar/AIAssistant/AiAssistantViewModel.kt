package com.example.findmycar.aiassistant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.data.AiMessage
import com.example.findmycar.data.AiRequest
import com.example.findmycar.data.AiResponse
import com.example.findmycar.data.MarketcheckListing
import com.example.findmycar.data.MarketcheckService
import com.example.findmycar.data.repository.ProfileRepository
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

    companion object {
        private const val TAG = "AiAssistantVM"
    }

    private val _uiState = MutableStateFlow(AiAssistantUiState())
    val uiState: StateFlow<AiAssistantUiState> = _uiState.asStateFlow()
    
    private val marketcheckService = MarketcheckService()
    private val profileRepository = ProfileRepository()
    private val json = Json { ignoreUnknownKeys = true }

    init {
        // Pre-fetch profile into cache
        viewModelScope.launch {
            try {
                profileRepository.getProfile()
                Log.d(TAG, "Profile pre-fetched successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to pre-fetch profile", e)
            }
        }
    }

    fun sendUserMessage(messageText: String) {
        if (messageText.isBlank()) return

        Log.d(TAG, "USER MESSAGE SENT: $messageText")
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
                Log.d(TAG, "Starting AI request flow...")
                // Get cached profile
                val profile = profileRepository.getProfile()
                Log.d(TAG, "Using profile for request: ${profile?.fullName ?: "Guest"}")

                // Convert UI messages to history format
                val history = _uiState.value.messages.map {
                    AiMessage(role = if (it.isUser) "user" else "assistant", content = it.content)
                }

                Log.d(TAG, "Invoking supabase function 'openai-chat' with ${history.size} messages")
                
                val httpResponse = supabase.functions.invoke(
                    function = "openai-chat",
                    body = AiRequest(
                        messages = history, 
                        mode = "general",
                        user_profile = profile
                    ),
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )

                val rawBody = httpResponse.bodyAsText()
                Log.d(TAG, "RAW RESPONSE FROM SUPABASE: $rawBody")
                val response = httpResponse.body<AiResponse>()

                val aiReplyText = response.output.firstOrNull()?.content?.firstOrNull()?.text 
                    ?: "AI returned an empty response."

                Log.d(TAG, "AI MESSAGE RECEIVED: $aiReplyText")
                val aiMessage = ChatMessage(content = aiReplyText, isUser = false)
                _uiState.update { it.copy(messages = it.messages + aiMessage, isLoading = false) }

            } catch (e: Exception) {
                Log.e(TAG, "CRITICAL ERROR getting AI response", e)
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to get AI response") }
            }
        }
    }
}
