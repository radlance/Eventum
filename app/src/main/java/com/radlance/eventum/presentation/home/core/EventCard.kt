package com.radlance.eventum.presentation.home.core

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.radlance.eventum.R
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.presentation.component.PriceRow
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.fillRedColor
import com.radlance.eventum.ui.theme.inputFieldTextColor
import com.radlance.eventum.ui.theme.poppinsFamily
import com.radlance.eventum.ui.theme.ralewayFamily
import com.radlance.eventum.ui.vector.FavoriteIcon

@Composable
fun EventCard(
    event: Event,
    onLikeClick: () -> Unit,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(interactionSource = interactionSource, indication = null) {
                onCardClick(event.id)
            }
    ) {
        Column {
            IconButton(
                onClick = onLikeClick,
                modifier = Modifier
                    .padding(start = 9.dp, top = 3.dp)
                    .clip(CircleShape)
                    .size(28.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                val fillColor = if (event.isFavorite) {
                    fillRedColor
                } else {
                    Color.LightGray
                }

                Image(
                    imageVector = FavoriteIcon(fillColor = fillColor),
                    contentDescription = "LikeIcon"
                )
            }

            SubcomposeAsyncImage(
                ImageRequest.Builder(context = LocalContext.current)
                    .data(event.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "event_example",
                contentScale = ContentScale.FillHeight,
                loading = {
                    Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Icon(
                        painter = painterResource(R.drawable.event_placeholder),
                        contentDescription
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(100.dp)
                    .animateContentSize()

                    .padding(horizontal = 21.dp)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = event.spendTime.uppercase(),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp,
                modifier = Modifier.padding(start = 9.dp)
            )

            Spacer(Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.height(60.dp)
            ) {
                Text(
                    text = event.title,
                    color = inputFieldTextColor,
                    fontSize = 16.sp,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 9.dp)
                )
            }

            PriceRow(
                price = event.pricesWithCategories.minOf { it.price }.toInt().toString()
            )
        }
    }
}


@Preview
@Composable
private fun EventCardPreview() {
    EventumTheme {
        EventCard(
            onLikeClick = {},
            modifier = Modifier.width(160.dp),
            event = Event(
                title = "mock",
                description = "",
                imageUrl = "https://",
                isFavorite = true,
                quantityInCart = 1,
                categoryId = 1,
                pricesWithCategories = emptyList(),
                spendTime = ""
            ),
            onCardClick = {}
        )
    }
}