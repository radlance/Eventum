package com.radlance.eventum.domain.search

import kotlinx.coroutines.flow.Flow

interface EventSearchRepository {
    fun loadSearchHistory(): Flow<List<SearchHistoryQuery>>
    suspend fun addQueryToHistory(searchHistoryQuery: SearchHistoryQuery)
}