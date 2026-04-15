package com.example.findmycar.data

import org.junit.Assert.assertEquals
import org.junit.Test

class MarketcheckMappingTest {

    @Test
    fun `toCar should correctly map MarketcheckListing fields`() {
        // Given
        val listing = MarketcheckListing(
            id = "123",
            price = 35000.0,
            miles = 12000,
            heading = "2023 Tesla Model 3",
            exterior_color = "Pearl White",
            build = MarketcheckBuild(
                year = 2023,
                make = "Tesla",
                model = "Model 3",
                fuel_type = "Electric",
                transmission = "Automatic",
                engine = "Electric Motor"
            ),
            media = MarketcheckMedia(
                photo_links = listOf("https://example.com/car.jpg")
            )
        )

        // When
        val car = listing.toCar()

        // Then
        assertEquals("123", car.id)
        assertEquals("Tesla", car.make)
        assertEquals("Model 3", car.model)
        assertEquals(2023, car.year)
        assertEquals(35000.0, car.price, 0.01)
        assertEquals(12000, car.mileage)
        assertEquals("Electric", car.fuelType)
        assertEquals("Automatic", car.transmission)
        assertEquals("Electric Motor", car.engineSize)
        assertEquals("Pearl White", car.color)
        assertEquals("2023 Tesla Model 3", car.description)
        assertEquals("https://example.com/car.jpg", car.imageUrl)
    }

    @Test
    fun `toCar should handle null build or media with default values`() {
        // Given
        val listing = MarketcheckListing(
            id = "456",
            price = 20000.0,
            miles = 50000,
            build = null,
            media = null
        )

        // When
        val car = listing.toCar()

        // Then
        assertEquals("456", car.id)
        assertEquals("", car.make)
        assertEquals("", car.model)
        assertEquals(0, car.year)
        assertEquals("", car.imageUrl)
    }
}
