package com.radlance.eventum.di

import com.radlance.eventum.domain.location.LocationClientResult
import com.radlance.eventum.presentation.common.LocationClientResultUiState
import com.radlance.eventum.presentation.order.LocationClientResultMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OrderModule {
    @Binds
    fun provideLocationClientResultMapper(
        locationClientResultMapper: LocationClientResultMapper
    ): LocationClientResult.Mapper<LocationClientResultUiState>
}