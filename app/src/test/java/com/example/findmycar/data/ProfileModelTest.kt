package com.example.findmycar.data.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileModelTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `Profile should serialize and deserialize correctly`() {
        // Given
        val profile = Profile(
            id = "1",
            userId = "user_123",
            fullName = "John Doe",
            email = "john@example.com",
            preferredBodyType = "SUV",
            features = listOf("Backup Camera", "Bluetooth")
        )

        // When
        val jsonString = json.encodeToString(profile)
        val decodedProfile = json.decodeFromString<Profile>(jsonString)

        // Then
        assertEquals(profile.userId, decodedProfile.userId)
        assertEquals(profile.fullName, decodedProfile.fullName)
        assertEquals(profile.preferredBodyType, decodedProfile.preferredBodyType)
        assertEquals(2, decodedProfile.features.size)
        assertTrue(decodedProfile.features.contains("Backup Camera"))
    }

    @Test
    fun `Profile should use default empty list for features if missing in JSON`() {
        // Given
        val jsonString = """
            {
                "user_id": "user_456",
                "full_name": "Jane Smith"
            }
        """.trimIndent()

        // When
        val profile = json.decodeFromString<Profile>(jsonString)

        // Then
        assertEquals("user_456", profile.userId)
        assertEquals("Jane Smith", profile.fullName)
        assertTrue(profile.features.isEmpty())
    }
}
