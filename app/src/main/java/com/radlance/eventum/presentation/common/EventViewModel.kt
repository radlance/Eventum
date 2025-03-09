package com.radlance.eventum.presentation.common

import android.util.Log
import com.radlance.eventum.domain.event.CatalogFetchContent
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.domain.event.EventCart
import com.radlance.eventum.domain.event.EventRepository
import com.radlance.eventum.domain.history.HistoryEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : BaseViewModel() {

    private val _historyResult =
        MutableStateFlow<FetchResultUiState<List<HistoryEvent>>>(FetchResultUiState.Initial())
    val historyResult: StateFlow<FetchResultUiState<List<HistoryEvent>>> =
        _historyResult.onStart { fetchHistory() }.stateInViewModel(
            FetchResultUiState.Loading()
        )

    private val _catalogContent =
        MutableStateFlow<FetchResultUiState<CatalogFetchContent>>(FetchResultUiState.Loading())
    val catalogContent: StateFlow<FetchResultUiState<CatalogFetchContent>> =
        _catalogContent.onStart { fetchContent() }.stateInViewModel(
            FetchResultUiState.Loading()
        )

    private val _cartContent =
        MutableStateFlow<FetchResultUiState<List<EventCart>>>(FetchResultUiState.Initial())
    val cartContent: StateFlow<FetchResultUiState<List<EventCart>>> =
        _cartContent.onStart { fetchCartContent() }.stateInViewModel(
            FetchResultUiState.Loading()
        )

    private val _placeOrderResult =
        MutableStateFlow<FetchResultUiState<List<EventCart>>>(FetchResultUiState.Initial())
    val placeOrderResult: StateFlow<FetchResultUiState<List<EventCart>>>
        get() = _placeOrderResult.asStateFlow()

    private val _favoriteResult =
        MutableStateFlow<FetchResultUiState<Int>>(FetchResultUiState.Initial())
    val favoriteResult: StateFlow<FetchResultUiState<Int>>
        get() = _favoriteResult.asStateFlow()

    private val _inCartResult =
        MutableStateFlow<FetchResultUiState<Int>>(FetchResultUiState.Initial())
    val inCartResult: StateFlow<FetchResultUiState<Int>>
        get() = _inCartResult.asStateFlow()

    private val _quantityResult =
        MutableStateFlow<FetchResultUiState<Int>>(FetchResultUiState.Initial())
    val quantityResult: StateFlow<FetchResultUiState<Int>>
        get() = _quantityResult.asStateFlow()

    private val _removeResult =
        MutableStateFlow<FetchResultUiState<Int>>(FetchResultUiState.Initial())
    val removeResult: StateFlow<FetchResultUiState<Int>>
        get() = _removeResult.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?>
        get() = _selectedEvent.asStateFlow()

    private val removedEventQuantity = MutableStateFlow(0)

    fun fetchHistory() {
        updateFetchUiState(_historyResult) { eventRepository.loadHistory() }
    }

    fun fetchContent() {
        updateFetchUiState(_catalogContent) { eventRepository.fetchCatalogContent() }
    }

    fun changeFavoriteStatus(eventId: Int) {
        updateFetchUiState(stateFlow = _favoriteResult, loadingData = eventId) {
            eventRepository.changeFavoriteStatus(eventId)
        }
    }

    fun addEventToCart(eventPriceId: Int, price: Double) {
        updateFetchUiState(stateFlow = _inCartResult, loadingData = eventPriceId) {
            eventRepository.addEventToCart(eventPriceId, price)
        }
    }

    fun updateEventQuantity(eventPriceId: Int, currentQuantity: Int) {
        updateFetchUiState(stateFlow = _quantityResult, loadingData = eventPriceId) {
            eventRepository.updateQuantity(eventPriceId, currentQuantity)
        }
    }

    fun removeEventFromCart(eventPriceId: Int) {
        updateFetchUiState(stateFlow = _removeResult, loadingData = eventPriceId) {
            eventRepository.removeEventFromCart(eventPriceId)
        }
    }

    fun fetchCartContent() {
        updateFetchUiState(stateFlow = _cartContent) {
            eventRepository.fetchCartContent()
        }
    }

    fun placeOrder(events: List<EventCart>) {
        updateFetchUiState(stateFlow = _placeOrderResult) {
            eventRepository.placeOrder(events)
        }
    }

    fun resetPlaceOrderResult() {
        _placeOrderResult.value = FetchResultUiState.Initial()
    }

    fun changeStateInCartStatus(
        eventPriceId: Int,
        event: Event? = null,
        selectedType: String = "",
        recover: Boolean = false
    ) {
        updateLocalState(_cartContent) { currentState ->
            Log.d("EventViewModel", event.toString())
            changeInCartByResult(currentState, eventPriceId, event, selectedType, recover)
        }
    }

    fun changeStateFavoriteStatus(eventId: Int) {
        updateLocalState(_catalogContent) { currentState ->
            changeFavoriteByResult(currentState, eventId)
        }
    }

    fun updateCurrentQuantity(eventId: Int, increment: Boolean) {
        updateLocalState(_cartContent) { currentState ->
            updateCartItemQuantity(currentState, eventId, increment)
        }
    }

    fun deleteCartItemFromCurrentState(eventPriceId: Int, recover: Boolean = false) {
        updateLocalState(_cartContent) { currentState ->
            deleteCartItem(currentState, eventPriceId, recover)
        }
    }

    fun updateStateAfterPlaceOrder(order: List<EventCart>) {
        updateLocalState(_catalogContent) { currentState ->
            updateCartItemsAfterPlaceOrder(currentState)
        }

        updateLocalState(_historyResult) { currentState ->
            saveOrderToHistory(currentState, order)
        }
    }

    private fun changeInCartByResult(
        currentState: FetchResultUiState.Success<List<EventCart>>,
        eventPriceId: Int,
        event: Event?,
        selectedType: String,
        recover: Boolean
    ) {
        val updatedEvents = currentState.data.map { eventCart ->
            if (eventCart.id == eventPriceId) {
                val newQuantity = if (recover) 0 else 1
                eventCart.copy(quantity = newQuantity)
            } else {
                eventCart
            }
        }.toMutableList()

        event?.let {
            if (updatedEvents == currentState.data) {
                updatedEvents.add(
                    EventCart(
                        event.id,
                        event,
                        event.pricesWithCategories.first { it.id == eventPriceId }.price,
                        quantity = if (recover) 0 else 1,
                        selectedType = selectedType
                    )
                )
            }
        }

        _cartContent.value = FetchResultUiState.Success(updatedEvents)
    }

    private fun changeFavoriteByResult(
        currentState: FetchResultUiState.Success<CatalogFetchContent>,
        eventId: Int
    ) {
        val updatedEvents = currentState.data.events.map { event ->
            if (event.id == eventId) {
                event.copy(isFavorite = !event.isFavorite)
            } else {
                event
            }
        }
        val updatedContent = currentState.data.copy(events = updatedEvents)
        _catalogContent.value = FetchResultUiState.Success(updatedContent)
    }

    private fun updateCartItemQuantity(
        currentState: FetchResultUiState.Success<List<EventCart>>,
        eventId: Int,
        increment: Boolean
    ) {
        val updatedEvents = currentState.data.map { event ->
            if (event.id == eventId) {
                val newQuantity =
                    if (increment) event.quantity.inc() else event.quantity.dec()
                event.copy(quantity = newQuantity)
            } else {
                event
            }
        }

        _cartContent.value = FetchResultUiState.Success(updatedEvents)
    }

    private fun deleteCartItem(
        currentState: FetchResultUiState.Success<List<EventCart>>,
        eventId: Int,
        recover: Boolean
    ) {
        val updatedEvents = currentState.data.map { event ->
            if (event.id == eventId) {
                if (!recover) {
                    removedEventQuantity.value = event.quantity
                    event.copy(quantity = 0)
                } else {
                    event.copy(quantity = removedEventQuantity.value)
                }
            } else {
                event
            }
        }
        _cartContent.value = FetchResultUiState.Success(updatedEvents)
    }

    private fun updateCartItemsAfterPlaceOrder(
        currentState: FetchResultUiState.Success<CatalogFetchContent>
    ) {
        val updatedEvents = currentState.data.events.map { event ->
            event.copy(quantityInCart = 0)
        }
        val updatedContent = currentState.data.copy(events = updatedEvents)
        _catalogContent.value = FetchResultUiState.Success(updatedContent)
    }

    private fun saveOrderToHistory(
        currentState: FetchResultUiState.Success<List<HistoryEvent>>,
        events: List<EventCart>
    ) {
        val currentHistory = currentState.data.toMutableList()
        currentHistory.addAll(
            events.map {
                with(it) {
                    HistoryEvent(
                        id = id,
                        title = event.title,
                        price = 0.0,
                        imageUrl = event.imageUrl,
                        priceType = "1 час",
                        orderTime = LocalDateTime.now().toKotlinLocalDateTime()
                    )
                }
            }
        )

        _historyResult.value = currentState.copy(data = currentHistory)
    }


    fun selectEvent(event: Event) {
        _selectedEvent.value = event
    }
}
