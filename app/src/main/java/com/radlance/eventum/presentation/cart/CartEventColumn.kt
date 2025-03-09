package com.radlance.eventum.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.domain.event.EventCart
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.poppinsFamily

@Composable
fun CartEventColumn(
    cartItems: List<EventCart>,
    onChangeQuantityClick: (eventPriceId: Int, quantity: Int, increment: Boolean) -> Unit,
    onRemoveEvent: (eventPriceId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = getEventCountText(cartItems.size),
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp
            )
        }

        items(items = cartItems, key = { eventCart -> eventCart.id }) { eventCart ->
            CartItemContainer(
                event = eventCart,
                onIncrement = {
                    onChangeQuantityClick(
                        eventCart.id,
                        eventCart.quantity.inc(),
                        true
                    )
                },
                onDecrement = {
                    onChangeQuantityClick(
                        eventCart.id,
                        eventCart.quantity.dec(),
                        false
                    )
                },

                onRemove = { onRemoveEvent(eventCart.id) }

            ) { item ->
                CartItem(
                    eventCart = item,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun getEventCountText(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "$count Товар"
        count % 10 in 2..4 && (count % 100 < 12 || count % 100 > 14) -> "$count Товара"
        else -> "$count Товаров"
    }
}

@Preview(showBackground = true)
@Composable
private fun CartEventColumnPreview() {
    EventumTheme {
        CartEventColumn(
            onRemoveEvent = {},
            onChangeQuantityClick = { _, _, _ -> },
            cartItems = List(20) {
                EventCart(
                    event = Event(
                        id = it,
                        title = "mock$it",
                        description = "",
                        imageUrl = "https://",
                        isFavorite = true,
                        quantityInCart = 1,
                        categoryId = 1,
                        pricesWithCategories = emptyList(),
                        spendTime = ""
                    ),
                    price = 1000.0,
                    quantity = 10,
                    selectedType = "1 час",
                    id = it
                )
            }
        )
    }
}