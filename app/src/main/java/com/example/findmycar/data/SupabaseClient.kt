package com.example.findmycar.data

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Shared instance of the Supabase client.
 *
 * Supabase said this API key is publishable, we jus need RLS on the database.
 * Might move these to .properties later, but this is fine for now.
 *
 */
val supabase = createSupabaseClient(
    supabaseUrl = "https://xwvggkyhtihiuzewcdix.supabase.co",
    supabaseKey = "sb_publishable_R8WzxfJux4JX4MPZ5KHURw_Pk0Od4yr"
) {
    install(Auth)
    install(Postgrest)
    install(Functions)
}
