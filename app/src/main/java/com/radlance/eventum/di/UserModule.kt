package com.radlance.eventum.di

import com.radlance.eventum.data.user.UserRepositoryImpl
import com.radlance.eventum.domain.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UserModule {
    @Binds
    fun provideRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}