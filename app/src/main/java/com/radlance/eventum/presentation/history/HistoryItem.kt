package com.radlance.eventum.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.radlance.eventum.domain.history.HistoryEvent
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.inputFieldTextColor
import com.radlance.eventum.ui.theme.poppinsFamily
import com.radlance.eventum.ui.theme.ralewayFamily
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun HistoryItem(
    historyEvent: HistoryEvent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(105.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row {
            Spacer(Modifier.width(9.dp))
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
                        .data(historyEvent.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "event_example",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.width(15.dp))
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(end = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = historyEvent.title,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 18.sp
                )
                
                Spacer(Modifier.height(6.dp))

                Text(
                    text = "${historyEvent.price.toInt()}₽",
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = historyEvent.priceType,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            }
        }

        val time = historyEvent.orderTime.time.toJavaLocalTime()
            .format(DateTimeFormatter.ofPattern("HH:mm"))

        Text(
            text = time,
            fontSize = 14.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            color = inputFieldTextColor,
            lineHeight = 20.sp,
            modifier = Modifier
                .padding(top = 10.dp, end = 10.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Preview
@Composable
private fun HistoryItemPreview() {
    EventumTheme {
        HistoryItem(
            historyEvent = HistoryEvent(
                id = 1,
                title = "mock",
                price = 100.0,
                imageUrl = "https://",
                priceType = "1 час",
                orderTime = LocalDateTime.now().toKotlinLocalDateTime()
            ),
        )
    }
}