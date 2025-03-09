package com.radlance.eventum.data.database.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartEntity(
    @SerialName("event_price_id") val eventPriceId: Int,
    val quantity: Int,
    @SerialName("user_id") val userId: String
)
