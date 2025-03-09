package com.radlance.eventum.presentation.home.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radlance.eventum.R
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.presentation.component.EventGrid
import com.radlance.eventum.presentation.home.common.ChangeEventStatus
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun SearchScreen(
    onBackPressed: () -> Unit,
    navigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val catalogContent by eventViewModel.catalogContent.collectAsState()
    val addToFavoriteResult by eventViewModel.favoriteResult.collectAsState()
    val addToCartResult by eventViewModel.inCartResult.collectAsState()

    val searchHistory by searchViewModel.localSearchHistory.collectAsState()

    var searchFieldValue by rememberSaveable { mutableStateOf("") }
    var searchSubmitQuery by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Spacer(Modifier.height(dimensionResource(R.dimen.main_top_padding)))
        SearchHeader(
            onBackPressed = {
                onBackPressed()
                keyboardController?.hide()
            }
        )
        Spacer(Modifier.height(26.dp))
        SearchField(
            value = searchFieldValue,
            onValueChange = {
                if (searchFieldValue.length < 64) {
                    searchFieldValue = it
                }
            },

            onSearchClick = {
                if (it.isNotBlank()) {
                    searchSubmitQuery = it
                    searchViewModel.addQueryToHistory(searchSubmitQuery)
                }
                keyboardController?.hide()
            },
            hint = stringResource(R.string.search),
            modifier = Modifier.padding(horizontal = 21.dp)
        )
        Spacer(Modifier.height(14.dp))

        addToFavoriteResult.Show(
            onSuccess = {},
            onLoading = { eventId ->
                ChangeEventStatus(eventId, eventViewModel::changeStateFavoriteStatus)
            },
            onError = { eventId ->
                ChangeEventStatus(eventId, eventViewModel::changeStateFavoriteStatus)
            },
            onUnauthorized = {}
        )

        addToCartResult.Show(
            onSuccess = {},
            onLoading = { eventId ->
                ChangeEventStatus(eventId, eventViewModel::changeStateInCartStatus)
            },
            onError = { eventId ->
                ChangeEventStatus(
                    eventId = eventId,
                    onStatusChanged = {
                        eventViewModel.changeStateInCartStatus(
                            it,
                            recover = true
                        )
                    }
                )
            },
            onUnauthorized = {}
        )

        if (searchSubmitQuery.isNotBlank()) {
            catalogContent.Show(
                onSuccess = { fetchContent ->
                    val foundedEvents = fetchContent.events.filter {
                        it.title.contains(searchSubmitQuery, ignoreCase = true)
                    }
                    if (foundedEvents.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Товаров по запросу \"$searchSubmitQuery\" не найдено",
                                fontSize = 16.sp,
                                fontFamily = ralewayFamily,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .offset(y = (-55).dp)
                                    .padding(horizontal = 21.dp)
                            )
                        }
                    } else {
                        EventGrid(
                            events = foundedEvents,
                            onLikeClicked = eventViewModel::changeFavoriteStatus,
                            onCardClick = navigateToDetails
                        )
                    }
                },

                onError = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(R.string.load_error))
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = eventViewModel::fetchContent) {
                                Text(stringResource(R.string.retry), color = Color.White)
                            }
                        }
                    }
                },

                onLoading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                onUnauthorized = {}
            )
        } else {
            SearchHistoryList(
                history = searchHistory,
                onHistoryQueryClick = {
                    searchSubmitQuery = it
                    searchFieldValue = it
                    keyboardController?.hide()
                    searchViewModel.addQueryToHistory(searchSubmitQuery)
                },
                modifier = Modifier.padding(horizontal = 21.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    EventumTheme {
        SearchScreen({}, {})
    }
}