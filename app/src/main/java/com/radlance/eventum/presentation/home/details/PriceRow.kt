package com.radlance.eventum.presentation.home.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.domain.event.PriceWithCategory
import com.radlance.eventum.presentation.home.core.ChosenItem
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun PriceRow(
    priceWithCategories: List<PriceWithCategory>,
    selectedPrice: Double,
    onPriceClick: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier.horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        priceWithCategories.forEach { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ChosenItem(
                    title = "${item.price.toInt()}₽",
                    selected = item.price == selectedPrice,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    size = DpSize(width = 137.dp, height = 50.dp),
                    modifier = Modifier.clickable { onPriceClick(item.price) }
                )

                Text(
                    text = item.title,
                    fontFamily = ralewayFamily,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
private fun PriceRowPreview() {
    EventumTheme {
        PriceRow(
            priceWithCategories = listOf(
                PriceWithCategory(price = 2000.0, title = "1 час", id = 1),
                PriceWithCategory(price = 5000.0, title = "2 часа", id = 2),
                PriceWithCategory(price = 500.0, title = "3 часа", id = 3)
            ),
            selectedPrice = 5000.0,
            onPriceClick = {}
        )
    }
}