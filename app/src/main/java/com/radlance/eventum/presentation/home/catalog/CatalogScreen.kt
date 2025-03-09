package com.radlance.eventum.presentation.home.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radlance.eventum.R
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.presentation.component.EventGrid
import com.radlance.eventum.presentation.home.common.CategoriesRow
import com.radlance.eventum.presentation.home.common.ChangeEventStatus
import com.radlance.eventum.ui.theme.EventumTheme

@Composable
fun CatalogScreen(
    selectedCategoryId: Int?,
    onBackPressed: () -> Unit,
    navigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val fetchResult by eventViewModel.catalogContent.collectAsState()
    val addToFavoriteResult by eventViewModel.favoriteResult.collectAsState()
    val addToCartResult by eventViewModel.inCartResult.collectAsState()

    var selectedCategoryIdState by rememberSaveable { mutableStateOf(selectedCategoryId) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(dimensionResource(R.dimen.main_top_padding)))
        fetchResult.Show(
            onSuccess = { fetchContent ->
                val selectedCategory = selectedCategoryIdState?.let {
                    fetchContent.categories.first { it.id == selectedCategoryIdState }
                }
                CatalogHeader(
                    title = selectedCategory?.title ?: stringResource(R.string.all),
                    onBackPressed = onBackPressed
                )
                Spacer(Modifier.height(16.dp))
                val events = selectedCategory?.let {
                    fetchContent.events.filter { it.categoryId == selectedCategory.id }
                } ?: fetchContent.events

                CategoriesRow(
                    categories = fetchContent.categories,
                    onCategoryClick = { selectedCategoryIdState = it },
                    selectedCategoryId = selectedCategoryIdState ?: 0
                )

                Spacer(Modifier.height(25.dp))

                EventGrid(
                    events = events,
                    onLikeClicked = eventViewModel::changeFavoriteStatus,
                    onCardClick = navigateToDetails
                )

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
                    CircularProgressIndicator(modifier = Modifier.offset(y = (-55).dp))
                }
            },

            onUnauthorized = {}
        )


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
                    onStatusChanged = { eventViewModel.changeStateInCartStatus(it, recover = true) }
                )
            },
            onUnauthorized = {}
        )
    }
}

@Preview
@Composable
private fun CatalogScreenPreview() {
    EventumTheme {
        CatalogScreen(
            selectedCategoryId = 1,
            onBackPressed = {},
            navigateToDetails = {}
        )
    }
}