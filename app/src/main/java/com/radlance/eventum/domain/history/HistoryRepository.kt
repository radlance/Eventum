package com.radlance.eventum.domain.history

import com.radlance.eventum.domain.remote.FetchResult

interface HistoryRepository {
    suspend fun loadHistory(): FetchResult<List<HistoryEvent>>
}