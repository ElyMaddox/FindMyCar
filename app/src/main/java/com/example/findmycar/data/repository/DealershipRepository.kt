package com.example.findmycar.data.repository

import com.example.findmycar.data.model.Dealership
import com.example.findmycar.data.supabase
import io.github.jan.supabase.postgrest.from

class DealershipRepository {

    private val table = "dealerships"

    suspend fun getDealerships(): List<Dealership> {
        return supabase.from(table)
            .select()
            .decodeList<Dealership>()
    }

    suspend fun getDealershipById(id: String): Dealership? {
        return supabase.from(table)
            .select { filter { eq("id", id) } }
            .decodeSingleOrNull<Dealership>()
    }
}
