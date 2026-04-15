package com.example.findmycar.data

import org.junit.Assert.assertEquals
import org.junit.Test

class CarTest {

    @Test
    fun `toDetailList should format price and mileage correctly`() {
        // Given
        val car = Car(
            make = "Toyota",
            model = "Camry",
            year = 2022,
            price = 25000.50,
            mileage = 15000,
            fuelType = "Gasoline",
            transmission = "Automatic",
            engineSize = "2.5L",
            horsepower = 203,
            color = "Blue"
        )

        // When
        val detailList = car.toDetailList()

        // Then
        val priceItem = detailList.find { it.label == "Price" }
        val mileageItem = detailList.find { it.label == "Mileage" }
        val makeItem = detailList.find { it.label == "Make" }

        assertEquals("$25000.50", priceItem?.value)
        assertEquals("15,000 miles", mileageItem?.value)
        assertEquals("Toyota", makeItem?.value)
    }

    @Test
    fun `toDetailList should contain all required fields`() {
        // Given
        val car = Car()

        // When
        val detailList = car.toDetailList()

        // Then
        val expectedLabels = listOf(
            "Make", "Model", "Year", "Price", "Mileage", 
            "Fuel Type", "Transmission", "Engine", "Horsepower", "Color"
        )
        val actualLabels = detailList.map { it.label }
        
        assertEquals(expectedLabels.size, actualLabels.size)
        expectedLabels.forEach { label ->
            assert(actualLabels.contains(label)) { "Missing label: $label" }
        }
    }
}
