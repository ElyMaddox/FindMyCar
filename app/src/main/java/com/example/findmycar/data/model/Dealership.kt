package com.example.findmycar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Dealership(
    val id: String,
    val name: String,
    val city: String,
    val state: String,
    val zip: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)
