package com.radlance.eventum.data.event

import com.radlance.eventum.data.common.BaseRepository
import com.radlance.eventum.domain.history.HistoryEvent
import com.radlance.eventum.domain.event.CatalogFetchContent
import com.radlance.eventum.domain.event.EventCart
import com.radlance.eventum.domain.event.EventRepository
import com.radlance.eventum.domain.remote.FetchResult
import io.github.jan.supabase.SupabaseClient
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    supabaseClient: SupabaseClient,
    localEventRepository: EventRepository,
    remoteEventRepository: EventRepository
) : EventRepository, BaseRepository<EventRepository>(
    supabaseClient,
    localEventRepository,
    remoteEventRepository
) {
    override suspend fun fetchCatalogContent(): FetchResult<CatalogFetchContent> {
        return getRepository().fetchCatalogContent()
    }

    override suspend fun changeFavoriteStatus(eventId: Int): FetchResult<Int> {
        return getRepository().changeFavoriteStatus(eventId)
    }

    override suspend fun fetchCartContent(): FetchResult<List<EventCart>> {
        return getRepository().fetchCartContent()
    }

    override suspend fun addEventToCart(eventPriceId: Int, price: Double): FetchResult<Int> {
        return getRepository().addEventToCart(eventPriceId, price)
    }

    override suspend fun updateQuantity(eventPriceId: Int, quantity: Int): FetchResult<Int> {
        return getRepository().updateQuantity(eventPriceId, quantity)
    }

    override suspend fun removeEventFromCart(eventId: Int): FetchResult<Int> {
        return getRepository().removeEventFromCart(eventId)
    }

    override suspend fun placeOrder(events: List<EventCart>): FetchResult<List<EventCart>> {
        return getRepository().placeOrder(events)
    }

    override suspend fun loadHistory(): FetchResult<List<HistoryEvent>> {
        return getRepository().loadHistory()
    }
}