package com.example.findmycar.data

import com.example.findmycar.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MarketcheckService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    suspend fun searchCars(
        make: String? = null,
        model: String? = null,
        zip: String? = null,
        radius: Int? = null,
        rows: Int = 10
    ): MarketcheckResponse {
        return try {
            client.get("https://marketcheck-prod.apigee.net/v2/search/car/active") {
                parameter("api_key", BuildConfig.MARKETCHECK_API_KEY)
                parameter("make", make)
                parameter("model", model)
                parameter("zip", zip)
                parameter("radius", radius)
                parameter("rows", rows)
                parameter("start", 0)
            }.body()
        } catch (e: Exception) {
            MarketcheckResponse()
        }
    }
}
