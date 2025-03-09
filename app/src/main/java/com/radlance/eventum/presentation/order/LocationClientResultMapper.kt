package com.radlance.eventum.presentation.order

import android.location.Address
import com.radlance.eventum.domain.location.LocationClientResult
import com.radlance.eventum.presentation.common.LocationClientResultUiState
import javax.inject.Inject

class LocationClientResultMapper @Inject constructor(
) : LocationClientResult.Mapper<LocationClientResultUiState> {
    override fun mapSuccess(address: Address): LocationClientResultUiState {
        return LocationClientResultUiState.Success(address)
    }

    override fun mapError(message: String): LocationClientResultUiState {
        return LocationClientResultUiState.Error(message)
    }
}