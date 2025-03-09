package com.radlance.eventum.di

import com.radlance.eventum.data.auth.AuthRepositoryImpl
import com.radlance.eventum.domain.authorization.AuthRepository
import com.radlance.eventum.common.ResourceManager
import com.radlance.eventum.common.ResourceManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Binds
    fun provideRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    fun provideResourceManager(resourceManager: ResourceManagerImpl): ResourceManager
}