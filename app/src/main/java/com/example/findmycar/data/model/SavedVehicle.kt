package com.example.findmycar.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedVehicle(
    val id: String,
    @SerialName("user_id") val userId: String,
    val make: String,
    val model: String,
    val year: Int,
    val price: Double,
    val mileage: Int,
    val vin: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("dealer_name") val dealerName: String? = null,
    @SerialName("saved_at") val savedAt: String
)
