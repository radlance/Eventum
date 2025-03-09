package com.radlance.eventum.di

import com.radlance.eventum.data.common.DataStoreRepository
import com.radlance.eventum.data.database.local.EventumDao
import com.radlance.eventum.data.notification.LocalNotificationRepository
import com.radlance.eventum.data.notification.NotificationDataStoreImpl
import com.radlance.eventum.data.notification.NotificationRepositoryImpl
import com.radlance.eventum.data.notification.RemoteNotificationRepository
import com.radlance.eventum.domain.notification.NotificationDataStore
import com.radlance.eventum.domain.notification.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationDataStore(
        dataStoreRepository: DataStoreRepository
    ): NotificationDataStore {
        return NotificationDataStoreImpl(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideLocalNotificationRepository(
        dao: EventumDao,
        notificationDataStore: NotificationDataStore
    ): LocalNotificationRepository {
        return LocalNotificationRepository(dao, notificationDataStore)
    }

    @Provides
    @Singleton
    fun provideRemoteEventRepository(
        supabaseClient: SupabaseClient,
        notificationDataStore: NotificationDataStore
    ): RemoteNotificationRepository {
        return RemoteNotificationRepository(supabaseClient, notificationDataStore)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        supabaseClient: SupabaseClient,
        localNotificationRepository: LocalNotificationRepository,
        remoteNotificationRepository: RemoteNotificationRepository
    ): NotificationRepository {
        return NotificationRepositoryImpl(
            supabaseClient,
            localNotificationRepository,
            remoteNotificationRepository
        )
    }
}