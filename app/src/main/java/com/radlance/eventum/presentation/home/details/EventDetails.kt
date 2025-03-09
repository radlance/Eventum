package com.radlance.eventum.presentation.home.details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.radlance.eventum.R
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.ui.theme.poppinsFamily
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun EventDetails(
    selectedEvent: Event,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = selectedEvent.title,
            fontSize = 26.sp,
            fontFamily = ralewayFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = 31.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "₽${selectedEvent.pricesWithCategories.minOf { it.price }}",
            fontSize = 24.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        SubcomposeAsyncImage(
            ImageRequest.Builder(context = LocalContext.current)
                .data(selectedEvent.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "event_example",
            contentScale = ContentScale.FillWidth,
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
                .padding(horizontal = 50.dp)
                .animateContentSize()
        )
    }
}