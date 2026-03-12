package com.example.findmycar.data

import android.util.Log
import com.example.findmycar.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MarketcheckService {

    companion object {
        private const val TAG = "MarketcheckService"
    }

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
        year: Int? = null,
        zip: String? = null,
        radius: Int? = null,
        rows: Int = 10
    ): MarketcheckResponse {
        return try {
            // Using the primary API endpoint domain
            val response = client.get("https://api.marketcheck.com/v2/search/car/active") {
                header("Accept", "application/json")
                parameter("api_key", BuildConfig.MARKETCHECK_API_KEY)
                
                make?.let { if (it.isNotBlank()) parameter("make", it.lowercase()) }
                model?.let { if (it.isNotBlank()) parameter("model", it.lowercase()) }
                year?.let { if (it > 0) parameter("year", it.toString()) }
                zip?.let { if (it.isNotBlank()) parameter("zip", it) }
                radius?.let { parameter("radius", it.toString()) }
                
                parameter("rows", rows.toString())
                parameter("start", "0")
                
                Log.d(TAG, "Request URL: ${this.url.build()}")
            }

            if (response.status.isSuccess()) {
                response.body()
            } else {
                val errorBody = response.bodyAsText()
                Log.e(TAG, "API Error ${response.status}: $errorBody")
                MarketcheckResponse()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network or Parsing Error: ${e.message}", e)
            MarketcheckResponse()
        }
    }
}
