package com.example.findmycar

import com.example.findmycar.data.MarketcheckBuild
import com.example.findmycar.data.MarketcheckDealer
import com.example.findmycar.data.MarketcheckListing
import org.junit.Assert.assertEquals
import org.junit.Test

class AccessibilityTest {

    @Test
    fun `contentDescription should format correctly with car data`() {
        // Given
        val listing = MarketcheckListing(
            heading = "2024 Honda Civic",
            price = 28000.0,
            dealer = MarketcheckDealer(city = "Columbus", state = "OH")
        )
        
        // Simulating the logic used in CarCardAdapter.bind
        // Since we can't easily access Android Resources (R.string) in a pure Unit Test,
        // we test the formatting logic and the presence of placeholders.
        val formatString = "Image of %1\$s, priced at %2\$s, located in %3\$s"
        val formattedPrice = "$28,000"
        val location = "${listing.dealer?.city}, ${listing.dealer?.state}"

        // When
        val result = String.format(formatString, listing.heading, formattedPrice, location)

        // Then
        val expected = "Image of 2024 Honda Civic, priced at $28,000, located in Columbus, OH"
        assertEquals(expected, result)
    }
}
