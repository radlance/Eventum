package com.radlance.eventum.presentation.home.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.R
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun SearchHistoryItem(
    queryMessage: String,
    onHistoryQueryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().clickable { onHistoryQueryClick(queryMessage) }
    ) {
        Image(painter = painterResource(R.drawable.ic_time), contentDescription = "ic_time")
        Spacer(Modifier.width(12.dp))
        Text(
            text = queryMessage,
            fontSize = 14.sp,
            fontFamily = ralewayFamily,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp
        )
    }
}

@Preview
@Composable
private fun SearchHistoryItemPreview() {
    EventumTheme {
        SearchHistoryItem(queryMessage = "New Event", onHistoryQueryClick = {})
    }
}