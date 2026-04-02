package com.example.findmycar.data

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Shared instance of the Supabase client, initialized lazily.
 */
val supabase by lazy {
    createSupabaseClient(
        supabaseUrl = "https://xwvggkyhtihiuzewcdix.supabase.co",
        supabaseKey = "sb_publishable_R8WzxfJux4JX4MPZ5KHURw_Pk0Od4yr"
    ) {
        install(Auth)
        install(Postgrest)
        install(Functions)
    }
}
