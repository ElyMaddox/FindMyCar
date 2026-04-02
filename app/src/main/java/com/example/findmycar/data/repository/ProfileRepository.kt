package com.example.findmycar.data.repository

import com.example.findmycar.data.model.Profile
import com.example.findmycar.data.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ProfileRepository {

    private val table = "profiles"
    
    companion object {
        private var cachedProfile: Profile? = null
        private val mutex = Mutex()
    }

    suspend fun getProfile(useCache: Boolean = true): Profile? {
        if (useCache) {
            mutex.withLock {
                cachedProfile?.let { return it }
            }
        }
        
        val userId = supabase.auth.currentUserOrNull()?.id ?: return null
        val profile = supabase.from(table)
            .select { filter { eq("user_id", userId) } }
            .decodeSingleOrNull<Profile>()
            
        mutex.withLock {
            cachedProfile = profile
        }
        return profile
    }

    suspend fun upsertProfile(profile: Profile) {
        supabase.from(table).upsert(profile)
        mutex.withLock {
            cachedProfile = profile
        }
    }

    suspend fun deleteProfile() {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        supabase.from(table).delete { filter { eq("user_id", userId) } }
        mutex.withLock {
            cachedProfile = null
        }
    }
    
    fun clearCache() {
        cachedProfile = null
    }
}
