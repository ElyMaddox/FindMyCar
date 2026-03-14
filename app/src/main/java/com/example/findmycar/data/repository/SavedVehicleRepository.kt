package com.example.findmycar.data.repository

import com.example.findmycar.data.model.SavedVehicle
import com.example.findmycar.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class SavedVehicleRepository {

    private val table = "saved_vehicles"

    suspend fun getSavedVehicles(): List<SavedVehicle> {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return emptyList()
        return supabase.from(table)
            .select { filter { eq("user_id", userId) } }
            .decodeList<SavedVehicle>()
    }

    suspend fun saveVehicle(vehicle: SavedVehicle) {
        supabase.from(table).insert(vehicle)
    }

    suspend fun deleteSavedVehicle(id: String) {
        supabase.from(table).delete { filter { eq("id", id) } }
    }

    suspend fun updateSavedVehicle(vehicle: SavedVehicle) {
        supabase.from(table).update(vehicle) { filter { eq("id", vehicle.id) } }
    }
}
