package com.example.findmycar.data.repository

import com.example.findmycar.data.model.Profile
import com.example.findmycar.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class ProfileRepository {

    private val table = "profiles"

    suspend fun getProfile(): Profile? {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return null
        return supabase.from(table)
            .select { filter { eq("user_id", userId) } }
            .decodeSingleOrNull<Profile>()
    }

    suspend fun upsertProfile(profile: Profile) {
        supabase.from(table).upsert(profile)
    }

    suspend fun deleteProfile() {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        supabase.from(table).delete { filter { eq("user_id", userId) } }
    }
}
