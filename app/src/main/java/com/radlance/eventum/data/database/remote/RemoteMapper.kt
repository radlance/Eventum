package com.radlance.eventum.data.database.remote

import com.radlance.eventum.data.database.remote.entity.EventPriceEntity
import com.radlance.eventum.data.database.remote.entity.HistoryEntity
import com.radlance.eventum.data.database.remote.entity.NotificationEntity
import com.radlance.eventum.data.database.remote.entity.RemoteCategoryEntity
import com.radlance.eventum.data.database.remote.entity.RemoteEventEntity
import com.radlance.eventum.data.database.remote.entity.UserEntity
import com.radlance.eventum.domain.event.Category
import com.radlance.eventum.domain.event.Event
import com.radlance.eventum.domain.event.EventCart
import com.radlance.eventum.domain.event.PriceWithCategory
import com.radlance.eventum.domain.notification.Notification
import com.radlance.eventum.domain.user.User
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

abstract class RemoteMapper {
    protected fun RemoteCategoryEntity.toCategory(): Category {
        return Category(id = id, title = title)
    }

    protected fun RemoteEventEntity.toEvent(
        isFavorite: Boolean,
        quantityInCart: Int,
        pricesWithCategories: List<PriceWithCategory>
    ): Event {
        return Event(
            id = id,
            title = title,
            description = description,
            imageUrl = imageUrl,
            isFavorite = isFavorite,
            quantityInCart = quantityInCart,
            categoryId = categoryId,
            pricesWithCategories = pricesWithCategories,
            spendTime = spendTime
        )
    }

    protected fun EventPriceEntity.toPriceWithCategory(): PriceWithCategory {
        return PriceWithCategory(id, priceType, price)
    }

    protected fun EventCart.toHistoryEntity(userId: String): HistoryEntity {
        return HistoryEntity(
            userId = userId,
            eventPriceId = id,
            quantity = quantity,
            orderDate = LocalDateTime.now().toKotlinLocalDateTime()
        )
    }

    protected fun UserEntity.toUser(): User {
        return User(
            firstName = name,
            imageUrl = imageUrl,
            phoneNumber = phoneNumber,
            address = address,
            lastName = lastName
        )
    }

    protected fun NotificationEntity.toNotification(): Notification {
        return Notification(
            title = title,
            message = message,
            sendDate = sendDate,
            isRead = isRead,
            id = id
        )
    }
}
