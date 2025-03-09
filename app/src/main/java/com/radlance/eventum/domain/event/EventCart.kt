package com.radlance.eventum.domain.event

data class EventCart(
    val id: Int,
    val event: Event,
    val price: Double,
    val quantity: Int,
    val selectedType: String
)
