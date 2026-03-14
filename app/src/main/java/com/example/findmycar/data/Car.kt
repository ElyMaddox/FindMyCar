package com.example.findmycar.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
@Parcelize
data class Car(
    val id: String? = null,
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
) : Parcelable {
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

@Serializable
data class CarDetailItem(
    val label: String,
    val value: String
)
