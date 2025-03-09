package com.radlance.eventum.di

import com.radlance.eventum.data.database.local.EventumDao
import com.radlance.eventum.data.event.LocalEventRepository
import com.radlance.eventum.data.event.EventRepositoryImpl
import com.radlance.eventum.data.event.RemoteEventRepository
import com.radlance.eventum.domain.event.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventModule {
    @Provides
    @Singleton
    fun provideLocalEventRepository(dao: EventumDao): LocalEventRepository {
        return LocalEventRepository(dao)
    }

    @Provides
    @Singleton
    fun provideRemoteEventRepository(
        supabaseClient: SupabaseClient,
        dao: EventumDao
    ): RemoteEventRepository {
        return RemoteEventRepository(supabaseClient, dao)
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        localRepository: LocalEventRepository,
        remoteRepository: RemoteEventRepository,
        supabaseClient: SupabaseClient
    ): EventRepository {
        return EventRepositoryImpl(supabaseClient, localRepository, remoteRepository)
    }
}