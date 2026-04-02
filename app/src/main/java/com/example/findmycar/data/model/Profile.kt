package com.example.findmycar.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String? = null,
    @SerialName("user_id") val userId: String,
    @SerialName("full_name") val fullName: String,
    val email: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("preferredBodyStyle") val preferredBodyType: String? = null,
    @SerialName("preferredDrivetrain") val preferredDrivetrain: String? = null,
    val features: List<String> = emptyList()
)
