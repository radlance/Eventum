package com.radlance.eventum.presentation.home.details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.R
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.ui.theme.inputFieldTextColor
import com.radlance.eventum.ui.theme.poppinsFamily

@Composable
fun EventDetailsDescription(
    selectedEvent: Event,
    modifier: Modifier = Modifier
) {
    var displayShowMoreDetails by remember { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Text(
        text = selectedEvent.description,
        fontSize = 14.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = inputFieldTextColor,
        maxLines = if (expanded) {
            Int.MAX_VALUE
        } else {
            3
        },
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowHeight) {
                displayShowMoreDetails = true
            }
        },
        modifier = Modifier.animateContentSize()
    )
    Spacer(Modifier.height(18.dp))

    if (displayShowMoreDetails) {
        val moreDetailsText = if (expanded) {
            R.string.hide
        } else {
            R.string.more
        }
        Text(
            text = stringResource(moreDetailsText),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Normal,
            lineHeight = 21.sp,
            modifier = modifier.clickable {
                expanded = !expanded
            }
        )
    }
}