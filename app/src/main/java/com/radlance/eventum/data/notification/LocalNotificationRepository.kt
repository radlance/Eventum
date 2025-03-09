package com.radlance.eventum.data.notification

import com.radlance.eventum.data.database.local.LocalMapper
import com.radlance.eventum.data.database.local.EventumDao
import com.radlance.eventum.domain.notification.Notification
import com.radlance.eventum.domain.notification.NotificationDataStore
import com.radlance.eventum.domain.notification.NotificationRepository
import com.radlance.eventum.domain.remote.FetchResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalNotificationRepository @Inject constructor(
    private val dao: EventumDao,
    private val notificationDataStore: NotificationDataStore
) : NotificationRepository, LocalMapper() {
    override suspend fun loadNotifications(): FetchResult<List<Notification>> {
        val notifications = dao.getNotifications()
        return FetchResult.Success(
            notifications.map { notificationEntity ->
                notificationEntity.toNotification()
            }
        )
    }

    override fun getNotificationsCount(): Flow<Int> {
        return notificationDataStore.getNotificationsCount()
    }

    override suspend fun updateNotificationsCount(notificationsCount: Int) {
        notificationDataStore.updateNotificationsCount(notificationsCount)
    }

    override suspend fun setNotificationRead(notificationId: Int): FetchResult<Unit> {
        dao.setNotificationRead(notificationId)
        return FetchResult.Success(Unit)
    }
}