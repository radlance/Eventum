package com.radlance.eventum.presentation.cart

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.domain.event.EventCart
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.poppinsFamily
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun CartItem(
    eventCart: EventCart,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .height(104.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.width(9.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(87.dp)
                    .fillMaxHeight()
                    .padding(top = 9.dp, bottom = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceTint)
            ) {
                AsyncImage(
                    ImageRequest.Builder(context = LocalContext.current)
                        .data(eventCart.event.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "event_example",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.width(30.dp))

            Column {
                Text(
                    text = eventCart.event.title,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.animateContentSize()
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "${eventCart.price.toInt()}₽",
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = eventCart.selectedType.uppercase(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun CartItemPreview() {
    EventumTheme {
        CartItem(
            eventCart =
                EventCart(
                    event = Event(
                        id = 1,
                        title = "mock",
                        description = "",
                        imageUrl = "https://",
                        isFavorite = true,
                        quantityInCart = 1,
                        categoryId = 1,
                        pricesWithCategories = emptyList(),
                        spendTime = ""
                    ),
                    price = 1000.0,
                    quantity = 0,
                    selectedType = "1 час",
                    id = 1
                )

        )
    }
}