package com.example.findmycar.data

import com.example.findmycar.data.model.Profile
import kotlinx.serialization.Serializable

@Serializable
data class AiMessage(val role: String, val content: String)

@Serializable
data class AiRequest(
    val messages: List<AiMessage>,
    val mode: String,
    val user_profile: Profile? = null
)

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
