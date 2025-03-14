package com.radlance.eventum.presentation.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.radlance.eventum.domain.notification.Notification
import com.radlance.eventum.ui.theme.EventumTheme
import kotlinx.coroutines.delay
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

@Composable
fun NotificationList(
    notifications: List<Notification>,
    onNotificationRead: (Notification) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 20.dp)
    ) {
        items(items = notifications, key = { notification -> notification.id }) { notification ->
            LaunchedEffect(notification.id) {
                delay(1000)
                onNotificationRead(notification)
            }
            NotificationItem(notification = notification)
        }
    }
}

@Preview
@Composable
private fun NotificationListPreview() {
    EventumTheme {
        NotificationList(
            onNotificationRead = {},
            notifications = List(20) {
                Notification(
                    id = it,
                    title = "Заголовок$it",
                    message = LoremIpsum(25).values.single(),
                    isRead = false,
                    sendDate = LocalDateTime.now().toKotlinLocalDateTime()
                )
            }
        )
    }
}