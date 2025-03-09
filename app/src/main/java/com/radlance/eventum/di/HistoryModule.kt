package com.radlance.eventum.di

import com.radlance.eventum.data.search.EventSearchRepositoryImpl
import com.radlance.eventum.domain.search.EventSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HistoryModule {
    @Binds
    fun provideRepository(eventSearchRepository: EventSearchRepositoryImpl): EventSearchRepository
}