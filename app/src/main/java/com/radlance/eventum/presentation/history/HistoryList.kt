package com.radlance.eventum.presentation.history

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.R
import com.radlance.eventum.domain.history.HistoryEvent
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryList(
    history: List<HistoryEvent>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 19.dp, end = 21.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        history.groupByDate(context).forEach { (date, events) ->
            item {
                Text(
                    text = date,
                    fontSize = 18.sp,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 22.sp
                )
            }

            items(
                items = events,
                key = { historyEvent -> historyEvent.id }) { historyEvent ->
                HistoryItem(historyEvent)
            }
            item { Spacer(Modifier.height(12.dp)) }
        }

    }
}

private fun List<HistoryEvent>.groupByDate(context: Context): Map<String, List<HistoryEvent>> {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val sortedHistory = sortedByDescending { it.orderTime }

    val groupedHistoryByDate: Map<String, List<HistoryEvent>> = sortedHistory.groupBy { historyEvent ->
        val localDate =
            historyEvent.orderTime.date
        when (localDate) {
            currentDate -> context.getString(R.string.recently)
            currentDate.minus(1, DateTimeUnit.DAY) -> context.getString(R.string.yesterday)
            else -> localDate.toJavaLocalDate()
                .format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault()))
        }
    }

    return groupedHistoryByDate
}

@Preview(showBackground = true)
@Composable
private fun HistoryListPreview() {
    EventumTheme {
        HistoryList(
            List(20) {
                HistoryEvent(
                    id = it,
                    title = "mock$it",
                    price = 100.0 * it,
                    imageUrl = "https://",
                    priceType = "1 час",
                    orderTime = LocalDateTime.now().toKotlinLocalDateTime()
                )
            }
        )
    }
}