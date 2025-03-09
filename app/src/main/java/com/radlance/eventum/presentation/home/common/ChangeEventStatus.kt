package com.radlance.eventum.presentation.home.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ChangeEventStatus(
    eventId: Int?,
    onStatusChanged: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        eventId?.let { onStatusChanged(eventId) }
    }
}
