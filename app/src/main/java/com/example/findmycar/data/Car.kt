package com.example.findmycar.data

import kotlinx.serialization.Serializable
import java.util.Locale

/**
 * This is temporary, gemini made this rq as a placeholder
 * it might be right though idk
 */
@Serializable
data class Car(
    val id: Int? = null,
    val make: String = "",
    val model: String = "",
    val year: Int = 0,
    val price: Double = 0.0,
    val mileage: Int = 0,
    val fuelType: String = "",
    val transmission: String = "",
    val engineSize: String = "",
    val horsepower: Int = 0,
    val color: String = "",
    val description: String = "",
    val imageUrl: String = ""
) {
    fun toDetailList(): List<CarDetailItem> {
        return listOf(
            CarDetailItem("Make", make),
            CarDetailItem("Model", model),
            CarDetailItem("Year", year.toString()),
            CarDetailItem("Price", String.format(Locale.US, "$%.2f", price)),
            CarDetailItem("Mileage", String.format(Locale.US, "%,d miles", mileage)),
            CarDetailItem("Fuel Type", fuelType),
            CarDetailItem("Transmission", transmission),
            CarDetailItem("Engine", engineSize),
            CarDetailItem("Horsepower", horsepower.toString()),
            CarDetailItem("Color", color)
        )
    }
}

data class CarDetailItem(
    val label: String,
    val value: String
)
