package com.radlance.eventum.presentation.home.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radlance.eventum.R
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.presentation.home.common.ChangeEventStatus
import com.radlance.eventum.ui.theme.EventumTheme

@Composable
fun EventDetailsScreen(
    selectedEventId: Int,
    onBackPressed: () -> Unit,
    onNavigateToCart: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val catalogContent by viewModel.catalogContent.collectAsState()

    val addToFavoriteResult by viewModel.favoriteResult.collectAsState()
    val addToCartResult by viewModel.inCartResult.collectAsState()

    var selectedPrice by rememberSaveable { mutableDoubleStateOf(0.0) }
    var inCartCurrent by rememberSaveable { mutableStateOf(false) }

    addToFavoriteResult.Show(
        onSuccess = {},
        onLoading = { eventId ->
            ChangeEventStatus(eventId, viewModel::changeStateFavoriteStatus)
        },
        onError = { eventId ->
            ChangeEventStatus(eventId, viewModel::changeStateFavoriteStatus)
        },
        onUnauthorized = {},
    )



    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        catalogContent.Show(
            onSuccess = { fetchContent ->
                val selectedEvent by viewModel.selectedEvent.collectAsState()
                viewModel.selectEvent(fetchContent.events.first { it.id == selectedEventId })

                selectedEvent?.let { event ->

                    addToCartResult.Show(
                        onSuccess = {},
                        onLoading = { eventId ->
                            inCartCurrent = true
                            ChangeEventStatus(
                                eventId = eventId,
                                onStatusChanged = {
                                    viewModel.changeStateInCartStatus(
                                        it,
                                        event = event,
                                        selectedType = event.pricesWithCategories.first { priceWithCategory ->
                                            priceWithCategory.price == selectedPrice
                                        }.title
                                    )
                                },
                            )
                        },
                        onError = { eventId ->
                            inCartCurrent = false
                            ChangeEventStatus(
                                eventId = eventId,
                                onStatusChanged = {
                                    viewModel.changeStateInCartStatus(
                                        it,
                                        recover = true,
                                        event = event
                                    )
                                }
                            )
                        },
                        onUnauthorized = {}
                    )

                    Spacer(Modifier.height(dimensionResource(R.dimen.main_top_padding)))

                    DetailsHeader(
                        onBackPressed = onBackPressed,
                        eventCategoryTitle = fetchContent.categories.first { it.id == selectedEvent?.categoryId }.title
                    )

                    Spacer(Modifier.height(26.dp))
                    EventDetails(selectedEvent = event)
                    Spacer(Modifier.height(20.dp))

                    PriceRow(
                        priceWithCategories = event.pricesWithCategories,
                        selectedPrice = selectedPrice,
                        onPriceClick = {
                            if (selectedPrice != it) {
                                selectedPrice = it
                                inCartCurrent = false
                            }
                        }
                    )

                    Spacer(Modifier.height(10.dp))

                    EventDetailsDescription(
                        selectedEvent = event,
                        modifier = Modifier.align(Alignment.End)
                    )


                    Spacer(Modifier.height(60.dp))

                    EventDetailsBottomContent(
                        isFavorite = event.isFavorite,
                        inCart = inCartCurrent,
                        onLikeClick = {
                            viewModel.changeFavoriteStatus(event.id)
                        },
                        onCartClick = {
                            viewModel.addEventToCart(
                                event.pricesWithCategories.first { it.price == selectedPrice }.id,
                                selectedPrice
                            )
                        },
                        buttonEnabled = selectedPrice != 0.0,
                        onNavigateToCart = onNavigateToCart
                    )
                }

            },
            onError = {},
            onLoading = {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            },
            onUnauthorized = {}
        )
    }
}


@Preview
@Composable
private fun EventDetailsScreenPreview() {
    EventumTheme {
        EventDetailsScreen(selectedEventId = 1, onBackPressed = {}, onNavigateToCart = {})
    }
}