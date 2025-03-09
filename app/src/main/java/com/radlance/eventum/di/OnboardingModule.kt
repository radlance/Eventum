package com.radlance.eventum.di

import com.radlance.eventum.data.onboarding.NavigationRepositoryImpl
import com.radlance.eventum.domain.onboarding.NavigationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OnboardingModule {
    @Binds
    fun provideRepository(onBoardingRepositoryImpl: NavigationRepositoryImpl): NavigationRepository
}