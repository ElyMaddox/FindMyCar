package com.example.findmycar.aiassistant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.data.MarketcheckListing
import com.example.findmycar.data.MarketcheckService
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

@Serializable
data class AiRequest(val message: String)

@Serializable
data class AiResponse(
    val output: List<AiOutput> = emptyList(),
    val tool_calls: List<AiToolCall>? = null
)

@Serializable
data class AiOutput(
    val content: List<AiContent> = emptyList()
)

@Serializable
data class AiContent(
    val text: String = ""
)

@Serializable
data class AiToolCall(
    val id: String,
    val name: String,
    val arguments: String
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
                Log.d("AiAssistantViewModel", "Sending message: $messageText")
                
                val httpResponse = supabase.functions.invoke(
                    function = "openai-chat",
                    body = AiRequest(message = messageText),
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )

                val rawBody = httpResponse.bodyAsText()
                Log.d("AiAssistantViewModel", "Raw Response: $rawBody")

                val response = httpResponse.body<AiResponse>()

                if (!response.tool_calls.isNullOrEmpty()) {
                    handleToolCalls(response.tool_calls)
                } else {
                    val aiReplyText = response.output.firstOrNull()?.content?.firstOrNull()?.text 
                        ?: "AI returned an empty response."

                    val aiMessage = ChatMessage(content = aiReplyText, isUser = false)
                    _uiState.update { it.copy(messages = it.messages + aiMessage, isLoading = false) }
                }

            } catch (e: Exception) {
                Log.e("AiAssistantViewModel", "Error getting AI response", e)
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to get AI response") }
            }
        }
    }

    private fun handleToolCalls(toolCalls: List<AiToolCall>) {
        viewModelScope.launch {
            try {
                val toolCall = toolCalls.first()
                if (toolCall.name == "search_cars") {
                    val argsJson = json.parseToJsonElement(toolCall.arguments).jsonObject
                    
                    val make = argsJson["make"]?.jsonPrimitive?.content
                    val model = argsJson["model"]?.jsonPrimitive?.content
                    val zip = argsJson["zip"]?.jsonPrimitive?.content
                    val radius = argsJson["radius"]?.jsonPrimitive?.content?.toIntOrNull() ?: 50

                    val response = marketcheckService.searchCars(
                        make = make,
                        model = model,
                        zip = zip,
                        radius = radius
                    )

                    val aiMessage = ChatMessage(
                        content = if (response.listings.isNotEmpty()) 
                            "I found ${response.listings.size} cars matching your search:" 
                            else "I couldn't find any cars matching those criteria.",
                        isUser = false,
                        carListings = response.listings
                    )
                    _uiState.update { it.copy(messages = it.messages + aiMessage, isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e("AiAssistantViewModel", "Error handling tool call", e)
                _uiState.update { it.copy(isLoading = false, error = "Error searching for cars") }
            }
        }
    }
}
