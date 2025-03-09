package com.radlance.eventum.data.common

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth

abstract class BaseRepository<T : Repository>(
    private val supabaseClient: SupabaseClient,
    private val localEventRepository: T,
    private val remoteEventRepository: T
) {
    protected fun getRepository(): T {
        return if (supabaseClient.auth.currentSessionOrNull()?.user == null) {
            localEventRepository
        } else {
            remoteEventRepository
        }
    }
}