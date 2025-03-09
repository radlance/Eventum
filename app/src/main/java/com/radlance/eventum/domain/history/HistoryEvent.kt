package com.radlance.eventum.domain.history

import kotlinx.datetime.LocalDateTime

data class HistoryEvent(
    val id: Int,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val orderTime: LocalDateTime,
    val priceType: String
)
