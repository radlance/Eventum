package com.radlance.eventum.domain.event

data class CatalogFetchContent(
    val categories: List<Category>,
    val events: List<Event>
)
