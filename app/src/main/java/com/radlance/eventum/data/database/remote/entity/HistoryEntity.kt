package com.radlance.eventum.data.database.remote.entity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryEntity(
    @SerialName("user_id") val userId: String,
    @SerialName("event_price_id") val eventPriceId: Int,
    val quantity: Int,
    @SerialName("order_date") val orderDate: LocalDateTime,
    val id: Int = 0
)