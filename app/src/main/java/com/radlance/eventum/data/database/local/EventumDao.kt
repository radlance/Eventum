package com.radlance.eventum.data.database.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.radlance.eventum.data.database.local.entity.LocalCategoryEntity
import com.radlance.eventum.data.database.local.entity.LocalNotificationEntity
import com.radlance.eventum.data.database.local.entity.LocalEventEntity
import com.radlance.eventum.data.database.local.entity.SearchHistoryQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventumDao {
    @Query("SELECT * FROM search_history_query ORDER BY query_time DESC LIMIT 6")
    fun getHistory(): Flow<List<SearchHistoryQueryEntity>>

    @Query("SELECT * FROM search_history_query WHERE `query` = :query LIMIT 1")
    suspend fun getSearchHistoryQuery(query: String): SearchHistoryQueryEntity?

    @Insert
    suspend fun insertSearchHistoryQuery(searchHistoryQuery: SearchHistoryQueryEntity)

    @Update
    suspend fun updateSearchHistoryQuery(searchHistoryQuery: SearchHistoryQueryEntity)

    @Query(
        """
        DELETE FROM search_history_query 
        WHERE id = (SELECT id FROM search_history_query ORDER BY query_time LIMIT 1)
    """
    )
    suspend fun removeOldestHistory()

    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<LocalCategoryEntity>

    @Query("SELECT * FROM event")
    suspend fun getEvents(): List<LocalEventEntity>

    @Query("UPDATE event SET is_favorite = NOT is_favorite WHERE id = :eventId")
    suspend fun changeFavoriteStatus(eventId: Int)

    @Query("UPDATE event SET quantity_in_cart = 1 WHERE id = :eventId")
    suspend fun addEventToCart(eventId: Int)

    @Query("SELECT * FROM event WHERE quantity_in_cart = 1")
    suspend fun getCartEvents(): List<LocalEventEntity>

    @Query("UPDATE event SET quantity_in_cart = :quantity WHERE id = :eventId")
    suspend fun updateEventQuantity(eventId: Int, quantity: Int)

    @Query("DELETE FROM event WHERE id = :eventId")
    suspend fun removeEventFromCart(eventId: Int)

    @Query("DELETE FROM event")
    suspend fun clearCart()

    @Query("SELECT * FROM notification")
    suspend fun getNotifications(): List<LocalNotificationEntity>

    @Query("UPDATE notification SET is_read = true WHERE id = :notificationId")
    suspend fun setNotificationRead(notificationId: Int)
}