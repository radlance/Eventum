package com.radlance.eventum.domain.event

import com.radlance.eventum.data.common.Repository
import com.radlance.eventum.domain.history.HistoryEvent
import com.radlance.eventum.domain.remote.FetchResult

interface EventRepository : Repository {
    suspend fun fetchCatalogContent(): FetchResult<CatalogFetchContent>
    suspend fun changeFavoriteStatus(eventId: Int): FetchResult<Int>
    suspend fun fetchCartContent(): FetchResult<List<EventCart>>
    suspend fun addEventToCart(eventPriceId: Int, price: Double): FetchResult<Int>
    suspend fun updateQuantity(eventPriceId: Int, quantity: Int): FetchResult<Int>
    suspend fun removeEventFromCart(eventId: Int): FetchResult<Int>
    suspend fun placeOrder(events: List<EventCart>): FetchResult<List<EventCart>>
    suspend fun loadHistory(): FetchResult<List<HistoryEvent>>
}
