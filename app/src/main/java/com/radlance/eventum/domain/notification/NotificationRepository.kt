package com.radlance.eventum.domain.notification

import com.radlance.eventum.data.common.Repository
import com.radlance.eventum.domain.remote.FetchResult
import kotlinx.coroutines.flow.Flow

interface NotificationDataStore {
    fun getNotificationsCount(): Flow<Int>
    suspend fun updateNotificationsCount(notificationsCount: Int)
}

interface NotificationRepository : Repository, NotificationDataStore {
    suspend fun loadNotifications(): FetchResult<List<Notification>>
    suspend fun setNotificationRead(notificationId: Int): FetchResult<Unit>
}