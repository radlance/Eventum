package com.radlance.eventum.data.database.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventPriceEntity(
    val id: Int = 0,
    @SerialName("event_id") val eventId: Int,
    val price: Double,
    @SerialName("price_type") val priceType: String
)
