package com.radlance.eventum.presentation.map

import com.radlance.eventum.presentation.common.BaseViewModel
import com.yandex.mapkit.MapKit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(mapKit: MapKit) : BaseViewModel() {
    val mapKitState = MutableStateFlow(mapKit).asStateFlow()
}