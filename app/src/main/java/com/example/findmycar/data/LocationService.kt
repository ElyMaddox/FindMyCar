package com.example.findmycar.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import java.util.Locale

class LocationService(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentZipCode(): String? {
        Log.d("LocationService", "GPS: Starting ZIP code fetch process...")
        return try {
            // 1. Try to get the fresh current location
            Log.d("LocationService", "GPS: Requesting current location (High Accuracy)...")
            var location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()

            // 2. Fallback to Last Known Location if current is null (common on emulators)
            if (location == null) {
                Log.d("LocationService", "GPS: Current location null, trying last known location...")
                location = fusedLocationClient.lastLocation.await()
            }

            if (location == null) {
                Log.e("LocationService", "GPS ERROR: Both current and last known location are NULL.")
                return null
            }

            Log.d("LocationService", "GPS: Found coordinates (${location.latitude}, ${location.longitude})")
            
            val geocoder = Geocoder(context, Locale.getDefault())
            
            // Geocoder can be flaky on emulators, let's wrap it carefully
            val addresses = try {
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            } catch (e: Exception) {
                Log.e("LocationService", "GEOCODER ERROR: Failed to reverse geocode: ${e.message}")
                null
            }
            
            if (addresses.isNullOrEmpty()) {
                Log.e("LocationService", "GPS ERROR: Geocoder returned no addresses.")
                return null
            }

            val zipCode = addresses.firstOrNull()?.postalCode
            if (zipCode == null) {
                Log.e("LocationService", "GPS ERROR: Address found, but it has no postalCode.")
            } else {
                Log.d("LocationService", "GPS SUCCESS: Resulting ZIP is $zipCode")
            }
            zipCode
        } catch (e: Exception) {
            Log.e("LocationService", "GPS CRITICAL FAILURE: ${e.message}", e)
            null
        }
    }
}
