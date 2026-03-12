package com.example.findmycar.interactivesearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.aiassistant.ChatMessage
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class InteractiveSearchUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class InteractiveSearchViewModel : ViewModel() {

    companion object {
        private const val TAG = "InteractiveSearchVM"
    }

    private val _uiState = MutableStateFlow(InteractiveSearchUiState())
    val uiState: StateFlow<InteractiveSearchUiState> = _uiState.asStateFlow()

    private val marketcheckService = MarketcheckService()
    private val json = Json { ignoreUnknownKeys = true }

    init {
        Log.d(TAG, "ViewModel Initialized")
        val welcomeMessage = ChatMessage(
            content = "Hi! I'm your interactive car search assistant. Tell me what kind of car you're looking for, or provide a ZIP code to start searching nearby!",
            isUser = false
        )
        _uiState.update { it.copy(messages = listOf(welcomeMessage)) }
    }

    fun sendUserMessage(messageText: String) {
        if (messageText.isBlank()) return

        Log.d(TAG, "sendUserMessage: $messageText")
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
                // Construct history from previous messages using shared models
                val history = _uiState.value.messages.map {
                    AiMessage(
                        role = if (it.isUser) "user" else "assistant",
                        content = it.content
                    )
                }

                Log.d(TAG, "Invoking supabase function with ${history.size} messages")
                
                val httpResponse = supabase.functions.invoke(
                    function = "openai-chat",
                    body = AiRequest(messages = history, mode = "car_search"),
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )

                val rawBody = httpResponse.bodyAsText()
                Log.d(TAG, "Raw Response: $rawBody")
                val response = httpResponse.body<AiResponse>()

                if (!response.tool_calls.isNullOrEmpty()) {
                    handleToolCalls(response.tool_calls!!)
                } else {
                    val aiReplyText = response.output.firstOrNull()?.content?.firstOrNull()?.text 
                        ?: "I'm sorry, I couldn't process that. Try asking about a specific car or location."

                    Log.d(TAG, "AI Response: $aiReplyText")
                    val aiMessage = ChatMessage(content = aiReplyText, isUser = false)
                    _uiState.update { it.copy(messages = it.messages + aiMessage, isLoading = false) }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error getting AI response", e)
                _uiState.update { it.copy(isLoading = false, error = "Failed to connect to the assistant.") }
            }
        }
    }

    private fun handleToolCalls(toolCalls: List<com.example.findmycar.data.AiToolCall>) {
        viewModelScope.launch {
            try {
                val toolCall = toolCalls.first()
                Log.d(TAG, "Handling Tool Call: ${toolCall.name} with args: ${toolCall.arguments}")
                if (toolCall.name == "search_cars") {
                    val argsJson = json.parseToJsonElement(toolCall.arguments).jsonObject
                    
                    val make = argsJson["make"]?.jsonPrimitive?.content
                    val model = argsJson["model"]?.jsonPrimitive?.content
                    val year = argsJson["year"]?.jsonPrimitive?.content?.toIntOrNull()
                    val zip = argsJson["zip"]?.jsonPrimitive?.content
                    val radius = argsJson["radius"]?.jsonPrimitive?.content?.toIntOrNull() ?: 50

                    Log.d(TAG, "Searching Marketcheck: make=$make, model=$model, year=$year, zip=$zip, radius=$radius")
                    val response = marketcheckService.searchCars(
                        make = make,
                        model = model,
                        year = year,
                        zip = zip,
                        radius = radius
                    )

                    Log.d(TAG, "Marketcheck found ${response.listings.size} listings")
                    val aiMessage = ChatMessage(
                        content = if (response.listings.isNotEmpty()) 
                            "I found ${response.listings.size} matching cars for you:" 
                            else "I found no listings for that search. Try broadening your criteria!",
                        isUser = false,
                        carListings = response.listings
                    )
                    _uiState.update { it.copy(messages = it.messages + aiMessage, isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error handling tool call", e)
                _uiState.update { it.copy(isLoading = false, error = "Error searching for cars") }
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "ViewModel Cleared")
        super.onCleared()
    }
}
