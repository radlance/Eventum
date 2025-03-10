package com.radlance.eventum.data.database.local

import com.radlance.eventum.data.database.local.entity.EventWithPrices
import com.radlance.eventum.data.database.local.entity.LocalCategoryEntity
import com.radlance.eventum.data.database.local.entity.LocalNotificationEntity
import com.radlance.eventum.data.database.local.entity.SearchHistoryQueryEntity
import com.radlance.eventum.domain.event.Category
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.domain.notification.Notification
import com.radlance.eventum.domain.search.SearchHistoryQuery

abstract class LocalMapper {
    protected fun SearchHistoryQueryEntity.toSearchHistoryQuery(): SearchHistoryQuery {
        return SearchHistoryQuery(query = query, queryTime = queryTime, id = id)
    }

    protected fun SearchHistoryQuery.toSearchHistoryQueryEntity(): SearchHistoryQueryEntity {
        return SearchHistoryQueryEntity(query = query, queryTime = queryTime)
    }

    protected fun LocalCategoryEntity.toCategory(): Category {
        return Category(title = title, id = id)
    }

    //FIXME
    protected fun EventWithPrices.toEvent(): Event {
        return Event(
            title = event.title,
            description = event.description,
            imageUrl = event.imageUrl,
            categoryId = event.categoryId,
            isFavorite = event.isFavorite,
            quantityInCart = 0,
            pricesWithCategories = emptyList(),
            spendTime = ""
        )
    }

    protected fun LocalNotificationEntity.toNotification(): Notification {
        return Notification(
            title = title,
            message = message,
            sendDate = sendDate,
            isRead = isRead,
            id = id
        )
    }
}