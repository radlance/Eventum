package com.radlance.eventum.presentation.home.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.radlance.eventum.ui.theme.EventumTheme

@Composable
fun HomeSearchBar(
    onSearchFieldClick: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = String()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HomeSearchField(
            hint = hint,
            onClick = onSearchFieldClick,
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(14.dp))
    }
}

@Preview
@Composable
private fun HomeSearchBarPreview() {
    EventumTheme {
        HomeSearchBar(onSearchFieldClick = {}, hint = "Поиск")
    }
}