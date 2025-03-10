package com.radlance.eventum.data.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "event_price",
    foreignKeys = [
        ForeignKey(
            entity = LocalEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_id"]
        )
    ]
)
data class LocalEventPriceEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "event_id", index = true) val eventId: Int,
    val price: Int,
    @ColumnInfo(name = "price_type") val priceType: String,
    @ColumnInfo(name = "quantity_in_cart") val quantityInCart: Int
)
