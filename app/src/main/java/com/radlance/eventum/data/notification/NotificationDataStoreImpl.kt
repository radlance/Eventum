package com.radlance.eventum.data.notification

import com.radlance.eventum.data.common.DataStoreRepository
import com.radlance.eventum.domain.notification.NotificationDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationDataStoreImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : NotificationDataStore {
    override fun getNotificationsCount(): Flow<Int> {
        return dataStoreRepository.getNotificationsCount()
    }

    override suspend fun updateNotificationsCount(notificationsCount: Int) {
        dataStoreRepository.updateNotificationCount(notificationsCount)
    }
}