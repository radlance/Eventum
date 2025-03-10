package com.radlance.eventum.data.database.local.entity

import androidx.room.Embedded
import androidx.room.Relation

//FIXME
data class EventWithPrices(
    @Embedded val event: LocalEventEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "event_id"
    )
    val prices: List<LocalEventPriceEntity>
)