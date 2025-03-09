package com.radlance.eventum.di

import com.radlance.eventum.domain.authorization.AuthResult
import com.radlance.eventum.presentation.authorization.common.AuthResultMapper
import com.radlance.eventum.presentation.authorization.common.AuthResultUiState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SignUpModule {
    @Binds
    fun provideMapper(mapper: AuthResultMapper): AuthResult.Mapper<AuthResultUiState>
}