package com.radlance.eventum.data.event

import com.radlance.eventum.data.database.local.EventumDao
import com.radlance.eventum.data.database.remote.RemoteMapper
import com.radlance.eventum.data.database.remote.entity.CartEntity
import com.radlance.eventum.data.database.remote.entity.EventPriceEntity
import com.radlance.eventum.data.database.remote.entity.FavoriteEntity
import com.radlance.eventum.data.database.remote.entity.HistoryEntity
import com.radlance.eventum.data.database.remote.entity.RemoteCategoryEntity
import com.radlance.eventum.data.database.remote.entity.RemoteEventEntity
import com.radlance.eventum.domain.event.CatalogFetchContent
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.domain.event.EventCart
import com.radlance.eventum.domain.event.EventRepository
import com.radlance.eventum.domain.history.HistoryEvent
import com.radlance.eventum.domain.remote.FetchResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import javax.inject.Inject

class RemoteEventRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val dao: EventumDao
) : EventRepository, RemoteMapper() {
    override suspend fun fetchCatalogContent(): FetchResult<CatalogFetchContent> {
        val user =
            supabaseClient.auth.currentSessionOrNull()?.user ?: return FetchResult.Error(null)

        return try {
            val localCartEntities = dao.getCartItems()

            if (localCartEntities.isNotEmpty()) {
                val userEvents = supabaseClient.from("cart").select {
                    filter { CartEntity::userId eq user.id }
                }.decodeList<CartEntity>()

                if (userEvents.isEmpty()) {


                    supabaseClient.from("cart").insert(localCartEntities.map { localEventPriceEntity ->
                        CartEntity(
                            eventPriceId = localEventPriceEntity.id,
                            quantity = localEventPriceEntity.quantityInCart,
                            userId = user.id
                        )
                    })
                    dao.clearCart()
                }
            }

            val categories =
                supabaseClient.from("category").select().decodeList<RemoteCategoryEntity>()
            val events = supabaseClient.from("event").select().decodeList<RemoteEventEntity>()
            val favoriteEvents = supabaseClient.from("favorite")
                .select { filter { FavoriteEntity::userId eq user.id } }
                .decodeList<FavoriteEntity>().associateBy { it.eventPriceId }
            val cartEvents =
                supabaseClient.from("cart").select { filter { CartEntity::userId eq user.id } }
                    .decodeList<CartEntity>().associateBy { it.eventPriceId }

            FetchResult.Success(
                CatalogFetchContent(
                    categories = categories.map { it.toCategory() },
                    events = events.map { event ->
                        val eventPrices = supabaseClient.from("event_price")
                            .select { filter { EventPriceEntity::eventId eq event.id } }
                            .decodeList<EventPriceEntity>()

                        event.toEvent(
                            isFavorite = favoriteEvents.containsKey(event.id),
                            quantityInCart = cartEvents[event.id]?.quantity ?: 0,
                            pricesWithCategories = eventPrices.map { eventPriceEntity ->
                                eventPriceEntity.toPriceWithCategory()
                            }
                        )
                    }
                )
            )
        } catch (e: Exception) {
            FetchResult.Error(null)
        }
    }


    override suspend fun changeFavoriteStatus(eventId: Int): FetchResult<Int> {
        val user =
            supabaseClient.auth.currentSessionOrNull()?.user ?: return FetchResult.Error(eventId)

        return try {
            val favorites = supabaseClient.from("favorite")
                .select {
                    filter {
                        FavoriteEntity::eventPriceId eq eventId
                        FavoriteEntity::userId eq user.id
                    }
                }
                .decodeList<FavoriteEntity>()

            if (favorites.isNotEmpty()) {
                supabaseClient.from("favorite").delete {
                    filter {
                        FavoriteEntity::eventPriceId eq eventId
                        FavoriteEntity::userId eq user.id
                    }
                }
            } else {
                supabaseClient.from("favorite").insert(
                    FavoriteEntity(eventPriceId = eventId, userId = user.id)
                )
            }

            FetchResult.Success(eventId)
        } catch (e: Exception) {
            FetchResult.Error(eventId)
        }
    }

    override suspend fun fetchCartContent(): FetchResult<List<EventCart>> {
        val user =
            supabaseClient.auth.currentSessionOrNull()?.user ?: return FetchResult.Error(null)

        return try {
            val cartItems = supabaseClient
                .from("cart")
                .select { filter { CartEntity::userId eq user.id } }
                .decodeList<CartEntity>()

            val eventPrices = supabaseClient
                .from("event_price")
                .select()
                .decodeList<EventPriceEntity>()
                .associateBy { it.id }

            val events = supabaseClient
                .from("event")
                .select()
                .decodeList<RemoteEventEntity>()
                .associateBy { it.id }

            val eventsCart = cartItems.mapNotNull { cart ->
                val eventPrice = eventPrices[cart.eventPriceId] ?: return@mapNotNull null
                val event = events[eventPrice.eventId] ?: return@mapNotNull null

                EventCart(
                    id = cart.eventPriceId,
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
                    price = eventPrice.price,
                    selectedType = eventPrice.priceType,
                    quantity = cart.quantity
                )
            }

            FetchResult.Success(eventsCart)
        } catch (e: Exception) {
            FetchResult.Error(null)
        }
    }

    override suspend fun addEventToCart(eventPriceId: Int, price: Double): FetchResult<Int> {
        val user = supabaseClient.auth.currentSessionOrNull()?.user
            ?: return FetchResult.Error(eventPriceId)

        return try {

            val cartItem = supabaseClient.from("cart").select {
                filter {
                    CartEntity::eventPriceId eq eventPriceId
                    CartEntity::userId eq user.id
                }
            }.decodeSingleOrNull<CartEntity>()

            val updatedEntity = cartItem?.copy(quantity = cartItem.quantity + 1)
                ?: CartEntity(eventPriceId = eventPriceId, userId = user.id, quantity = 1)

            supabaseClient.from("cart").upsert(updatedEntity)

            FetchResult.Success(eventPriceId)
        } catch (e: Exception) {
            FetchResult.Error(eventPriceId)
        }
    }


    override suspend fun updateQuantity(eventPriceId: Int, quantity: Int): FetchResult<Int> {
        val user =
            supabaseClient.auth.currentSessionOrNull()?.user ?: return FetchResult.Error(
                eventPriceId
            )
        return try {
            supabaseClient.from("cart").update(
                {
                    CartEntity::quantity setTo quantity
                }
            ) {
                filter {
                    CartEntity::eventPriceId eq eventPriceId
                    CartEntity::userId eq user.id
                }
            }

            FetchResult.Success(eventPriceId)
        } catch (e: Exception) {
            FetchResult.Error(eventPriceId)
        }
    }

    override suspend fun removeEventFromCart(eventId: Int): FetchResult<Int> {
        val user =
            supabaseClient.auth.currentSessionOrNull()?.user ?: return FetchResult.Error(eventId)

        return try {
            supabaseClient.from("cart").delete {
                filter {
                    CartEntity::eventPriceId eq eventId
                    CartEntity::userId eq user.id
                }
            }
            FetchResult.Success(eventId)
        } catch (e: Exception) {
            FetchResult.Error(eventId)
        }
    }

    override suspend fun placeOrder(events: List<EventCart>): FetchResult<List<EventCart>> {
        val user = supabaseClient.auth.currentSessionOrNull()?.user ?: return FetchResult.Error(
            emptyList()
        )

        return try {
            val historyEntities = events.map { it.toHistoryEntity(user.id) }

            supabaseClient.from("history").insert(historyEntities)

            supabaseClient.from("cart").delete {
                filter {
                    CartEntity::eventPriceId isIn events.map { it.id }
                    CartEntity::userId eq user.id
                }
            }

            FetchResult.Success(events)
        } catch (e: Exception) {
            FetchResult.Error(emptyList())
        }
    }

    override suspend fun loadHistory(): FetchResult<List<HistoryEvent>> {
        val user = supabaseClient.auth.currentSessionOrNull()?.user ?: return FetchResult.Error(
            emptyList()
        )

        return try {
            val historyEntities = supabaseClient
                .from("history")
                .select { filter { HistoryEntity::userId eq user.id } }
                .decodeList<HistoryEntity>()

            val eventPrices = supabaseClient
                .from("event_price")
                .select()
                .decodeList<EventPriceEntity>()
                .associateBy { it.id }

            val events = supabaseClient
                .from("event")
                .select()
                .decodeList<RemoteEventEntity>()
                .associateBy { it.id }

            val historyEvents: List<HistoryEvent> = historyEntities.mapNotNull { history ->
                val eventPrice = eventPrices[history.eventPriceId] ?: return@mapNotNull null
                val event = events[eventPrice.eventId] ?: return@mapNotNull null

                HistoryEvent(
                    eventPriceId = history.eventPriceId,
                    price = eventPrice.price * history.quantity,
                    title = event.title,
                    imageUrl = event.imageUrl,
                    priceType = eventPrice.priceType,
                    id = history.id,
                    orderTime = LocalDateTime.now().toKotlinLocalDateTime()
                )
            }

            FetchResult.Success(historyEvents)
        } catch (e: Exception) {
            FetchResult.Error(null)
        }
    }
}
