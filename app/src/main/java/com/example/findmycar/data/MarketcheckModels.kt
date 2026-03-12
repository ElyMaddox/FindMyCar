package com.example.findmycar.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketcheckResponse(
    val num_found: Int = 0,
    val listings: List<MarketcheckListing> = emptyList()
)

@Serializable
data class MarketcheckListing(
    val id: String = "",
    val vin: String = "",
    val heading: String = "",
    val price: Double = 0.0,
    val miles: Int = 0,
    val build: MarketcheckBuild? = null,
    val media: MarketcheckMedia? = null,
    val dealer: MarketcheckDealer? = null,
    val exterior_color: String = "",
    val interior_color: String = ""
)

@Serializable
data class MarketcheckBuild(
    val year: Int = 0,
    val make: String = "",
    val model: String = "",
    val trim: String = "",
    val body_type: String = "",
    val fuel_type: String = "",
    val transmission: String = "",
    val engine: String = ""
)

@Serializable
data class MarketcheckMedia(
    val photo_links: List<String> = emptyList()
)

@Serializable
data class MarketcheckDealer(
    val name: String = "",
    val city: String = "",
    val state: String = "",
    val zip: String = ""
)

/**
 * Extension to convert Marketcheck listing to our app's Car model
 */
fun MarketcheckListing.toCar(): Car {
    return Car(
        id = id.hashCode(), // Simplified for now
        make = build?.make ?: "",
        model = build?.model ?: "",
        year = build?.year ?: 0,
        price = price,
        mileage = miles,
        fuelType = build?.fuel_type ?: "",
        transmission = build?.transmission ?: "",
        engineSize = build?.engine ?: "",
        color = exterior_color,
        description = heading,
        imageUrl = media?.photo_links?.firstOrNull() ?: ""
    )
}
