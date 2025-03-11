package com.radlance.eventum.data.event

import com.radlance.eventum.data.database.local.EventumDao
import com.radlance.eventum.data.database.local.LocalMapper
import com.radlance.eventum.domain.event.CatalogFetchContent
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.domain.event.EventCart
import com.radlance.eventum.domain.event.EventRepository
import com.radlance.eventum.domain.history.HistoryEvent
import com.radlance.eventum.domain.remote.FetchResult
import javax.inject.Inject

class LocalEventRepository @Inject constructor(
    private val dao: EventumDao
) : EventRepository, LocalMapper() {
    override suspend fun fetchCatalogContent(): FetchResult<CatalogFetchContent> {
        val catalogFetchContent = CatalogFetchContent(
            categories = dao.getCategories().map { categoryEntity -> categoryEntity.toCategory() },
            events = dao.getEvents().map { eventEntity ->
                val localEventPriceEntities = dao.getEventPricesById(eventEntity.id)
                eventEntity.toEvent(
                    pricesWithCategories = localEventPriceEntities.map { entity ->
                        entity.toPriceWithCategory()
                    }
                )
            }
        )
        return FetchResult.Success(catalogFetchContent)
    }

    override suspend fun changeFavoriteStatus(eventId: Int): FetchResult<Int> {
        dao.changeFavoriteStatus(eventId)
        return FetchResult.Success(eventId)
    }

    override suspend fun fetchCartContent(): FetchResult<List<EventCart>> {
        val eventPrices = dao.getEventPrices()
        val events = dao.getEvents().associateBy { it.id }

        val eventsCart = eventPrices.mapNotNull { eventPrice ->
            val event = events[eventPrice.eventId] ?: return@mapNotNull null

            EventCart(
                id = eventPrice.id,
                event = Event(
                    id = event.id,
                    title = event.title,
                    imageUrl = event.imageUrl,
                    categoryId = event.categoryId,
                    description = event.description,
                    isFavorite = false,
                    quantityInCart = 0,
                    pricesWithCategories = emptyList(),
                    spendTime = event.spendTime
                ),
                price = eventPrice.price.toDouble(),
                selectedType = eventPrice.priceType,
                quantity = eventPrice.quantityInCart
            )
        }

        return FetchResult.Success(eventsCart)
    }

    override suspend fun addEventToCart(eventPriceId: Int, price: Double): FetchResult<Int> {
        dao.addEventToCart(eventPriceId)
        return FetchResult.Success(eventPriceId)
    }

    override suspend fun updateQuantity(eventPriceId: Int, quantity: Int): FetchResult<Int> {
        dao.updateEventQuantity(eventPriceId, quantity)
        return FetchResult.Success(eventPriceId)
    }

    override suspend fun removeEventFromCart(eventId: Int): FetchResult<Int> {
        dao.removeEventFromCart(eventId)
        return FetchResult.Success(eventId)
    }

    override suspend fun placeOrder(events: List<EventCart>): FetchResult<List<EventCart>> {
        return FetchResult.Unauthorized()
    }

    override suspend fun loadHistory(): FetchResult<List<HistoryEvent>> {
        return FetchResult.Unauthorized()
    }
}