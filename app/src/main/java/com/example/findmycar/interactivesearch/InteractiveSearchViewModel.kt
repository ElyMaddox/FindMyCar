package com.example.findmycar.interactivesearch

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.findmycar.aiassistant.ChatMessage
import com.example.findmycar.data.AiMessage
import com.example.findmycar.data.AiRequest
import com.example.findmycar.data.AiResponse
import com.example.findmycar.data.LocationService
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class InteractiveSearchUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class InteractiveSearchViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "InteractiveSearchVM"
    }

    private val _uiState = MutableStateFlow(InteractiveSearchUiState())
    val uiState: StateFlow<InteractiveSearchUiState> = _uiState.asStateFlow()

    private val marketcheckService = MarketcheckService()
    private val profileRepository = ProfileRepository()
    private val locationService = LocationService(application)
    private val json = Json { ignoreUnknownKeys = true }

    init {
        Log.d(TAG, "ViewModel Initialized")
        val welcomeMessage = ChatMessage(
            content = "Hi! I'm your interactive car search assistant. Tell me what kind of car you're looking for, or provide a ZIP code to start searching nearby!",
            isUser = false
        )
        _uiState.update { it.copy(messages = listOf(welcomeMessage)) }
        
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

    private fun containsZipCode(text: String): Boolean {
        // Simple regex to check for a 5-digit number
        return Regex("\\b\\d{5}\\b").containsMatchIn(text)
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

                // ANDROID-SIDE GPS FETCH:
                // If the user's message doesn't contain a zip code, fetch GPS and prepend it for the AI
                var finalMessageText = messageText
                if (!containsZipCode(messageText)) {
                    Log.d(TAG, "No ZIP found in user message. Fetching GPS...")
                    val gpsZip = locationService.getCurrentZipCode()
                    if (gpsZip != null) {
                        Log.d(TAG, "GPS SUCCESS: Found ZIP $gpsZip")
                        // Prepend as a system instruction at the beginning of the message
                        finalMessageText = "The user is currently at ZIP code $gpsZip. Use this for car searches if no other location is mentioned. User message: $messageText"
                        Log.d(TAG, "Prepended GPS context: $finalMessageText")
                    } else {
                        Log.d(TAG, "GPS FAILED: Could not determine ZIP code")
                    }
                }

                // Construct history using the augmented message for the latest entry
                val history = _uiState.value.messages.mapIndexed { index, chatMsg ->
                    AiMessage(
                        role = if (chatMsg.isUser) "user" else "assistant",
                        content = if (index == _uiState.value.messages.size - 1 && chatMsg.isUser) finalMessageText else chatMsg.content
                    )
                }

                Log.d(TAG, "Invoking supabase function 'openai-chat' with ${history.size} messages")
                
                val httpResponse = supabase.functions.invoke(
                    function = "openai-chat",
                    body = AiRequest(
                        messages = history, 
                        mode = "car_search",
                        user_profile = profile
                    ),
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )

                val rawBody = httpResponse.bodyAsText()
                Log.d(TAG, "RAW RESPONSE FROM SUPABASE: $rawBody")
                val response = httpResponse.body<AiResponse>()

                val toolCalls = response.tool_calls
                if (!toolCalls.isNullOrEmpty()) {
                    Log.d(TAG, "Function returned tool calls: ${toolCalls.size}")
                    handleToolCalls(toolCalls)
                } else {
                    val aiReplyText = response.output.firstOrNull()?.content?.firstOrNull()?.text 
                        ?: "I'm sorry, I couldn't process that. Try asking about a specific car or location."

                    Log.d(TAG, "AI MESSAGE RECEIVED: $aiReplyText")
                    val aiMessage = ChatMessage(content = aiReplyText, isUser = false)
                    _uiState.update { it.copy(messages = it.messages + aiMessage, isLoading = false) }
                }

            } catch (e: Exception) {
                Log.e(TAG, "CRITICAL ERROR getting AI response", e)
                _uiState.update { it.copy(isLoading = false, error = "Failed to connect to the assistant: ${e.message}") }
            }
        }
    }

    private fun handleToolCalls(toolCalls: List<com.example.findmycar.data.AiToolCall>) {
        viewModelScope.launch {
            try {
                val toolCall = toolCalls.first()
                Log.d(TAG, "Executing tool call: ${toolCall.name}")
                if (toolCall.name == "search_cars") {
                    val argsJson = json.parseToJsonElement(toolCall.arguments).jsonObject
                    
                    val make = argsJson["make"]?.jsonPrimitive?.content
                    val model = argsJson["model"]?.jsonPrimitive?.content
                    val year = argsJson["year"]?.jsonPrimitive?.content?.toIntOrNull()
                    val zip = argsJson["zip"]?.jsonPrimitive?.content
                    val radius = argsJson["radius"]?.jsonPrimitive?.content?.toIntOrNull() ?: 50

                    Log.d(TAG, "Marketcheck API Search Parameters: make=$make, model=$model, zip=$zip")
                    val response = marketcheckService.searchCars(
                        make = make,
                        model = model,
                        year = year,
                        zip = zip,
                        radius = radius
                    )

                    Log.d(TAG, "Marketcheck API Results: Found ${response.listings.size} cars")
                    val aiMessage = ChatMessage(
                        content = if (response.listings.isNotEmpty()) 
                            "I found ${response.listings.size} matching cars for you ${if (zip != null) "near $zip" else ""}:"
                            else "I found no listings for that search. Try broadening your criteria!",
                        isUser = false,
                        carListings = response.listings
                    )
                    _uiState.update { it.copy(messages = it.messages + aiMessage, isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in handleToolCalls", e)
                _uiState.update { it.copy(isLoading = false, error = "Error searching for cars: ${e.message}") }
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "ViewModel Cleared")
        super.onCleared()
    }
}
