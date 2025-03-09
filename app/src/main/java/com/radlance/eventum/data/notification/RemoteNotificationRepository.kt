package com.radlance.eventum.data.notification

import com.radlance.eventum.data.database.remote.RemoteMapper
import com.radlance.eventum.data.database.remote.entity.NotificationEntity
import com.radlance.eventum.domain.notification.Notification
import com.radlance.eventum.domain.notification.NotificationDataStore
import com.radlance.eventum.domain.notification.NotificationRepository
import com.radlance.eventum.domain.remote.FetchResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteNotificationRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val notificationDataStore: NotificationDataStore
) : NotificationRepository, RemoteMapper() {

    override suspend fun loadNotifications(): FetchResult<List<Notification>> {
        val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id
            ?: return FetchResult.Unauthorized()

        return try {
            val notificationEntities = supabaseClient.from("notification")
                .select {
                    filter { NotificationEntity::userId eq userId }
                }
                .decodeList<NotificationEntity>()

            FetchResult.Success(notificationEntities.map { it.toNotification() })
        } catch (e: Exception) {
            FetchResult.Error(emptyList())
        }
    }

    override fun getNotificationsCount(): Flow<Int> =
        notificationDataStore.getNotificationsCount()

    override suspend fun updateNotificationsCount(notificationsCount: Int) {
        notificationDataStore.updateNotificationsCount(notificationsCount)
    }

    override suspend fun setNotificationRead(notificationId: Int): FetchResult<Unit> {
        val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id
            ?: return FetchResult.Unauthorized()

        return try {
            supabaseClient.from("notification").update(
                { NotificationEntity::isRead setTo true }
            ) {
                filter {
                    NotificationEntity::id eq notificationId
                    NotificationEntity::userId eq userId
                }
            }
            FetchResult.Success(Unit)
        } catch (e: Exception) {
            FetchResult.Error(Unit)
        }
    }
}
