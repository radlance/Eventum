package com.radlance.eventum.domain.search

import kotlinx.datetime.LocalDateTime

data class SearchHistoryQuery(
    val query: String,
    val queryTime: LocalDateTime,
    val id: Int = 0
)
