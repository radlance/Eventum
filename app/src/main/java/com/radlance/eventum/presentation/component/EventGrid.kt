package com.radlance.eventum.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.presentation.home.core.EventCard
import com.radlance.eventum.ui.theme.EventumTheme

@Composable
fun EventGrid(
    events: List<Event>,
    onLikeClicked: (eventId: Int) -> Unit,
    onCardClick: (eventId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 65.dp, end = 65.dp, top = 20.dp, bottom = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        items(items = events, key = { event -> event.id }) { event ->
            EventCard(
                event = event,
                onLikeClick = { onLikeClicked(event.id) },
                onCardClick = onCardClick
            )
        }
    }
}

@Preview
@Composable
private fun EventGridPreview() {
    EventumTheme {
        EventGrid(
            events = List(20) {
                Event(
                    id = it,
                    title = "mock$it",
                    description = "",
                    imageUrl = "https://",
                    isFavorite = true,
                    quantityInCart = 1,
                    categoryId = 1,
                    pricesWithCategories = emptyList(),
                    spendTime = ""
                )
            },
            onLikeClicked = {},
            onCardClick = {},
        )
    }
}