package com.radlance.eventum.di

import android.content.Context
import com.radlance.eventum.BuildConfig
import com.radlance.eventum.data.database.local.EventumDao
import com.radlance.eventum.data.database.local.EventumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): EventumDatabase {
        return EventumDatabase.getInstance(context)
    }
    
    @Singleton
    @Provides
    fun provideDao(database: EventumDatabase): EventumDao {
        return database.dao()
    }

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideSupabaseClient(): SupabaseClient {
        val supabase = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
            defaultSerializer = KotlinXSerializer()
        }

        return supabase
    }
}