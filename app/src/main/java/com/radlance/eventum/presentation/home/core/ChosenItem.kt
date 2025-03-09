package com.radlance.eventum.presentation.home.core

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.poppinsFamily

@Composable
fun ChosenItem(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    size: DpSize = DpSize(width = 108.dp, height = 40.dp)

) {
    val boxBackgroundColor by animateColorAsState(
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )

    val textColor by animateColorAsState(
        if (selected) {
            Color.White
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(boxBackgroundColor)
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = fontSize,
            fontFamily = poppinsFamily,
            fontWeight = fontWeight,
            lineHeight = 18.sp
        )
    }
}

@Preview
@Composable
private fun CategoryItemUnselectedPreview() {
    EventumTheme {
        ChosenItem(title = "Tennis", selected = false)
    }
}

@Preview
@Composable
private fun CategoryItemSelectedPreview() {
    EventumTheme {
        ChosenItem(title = "Tennis", selected = true)
    }
}