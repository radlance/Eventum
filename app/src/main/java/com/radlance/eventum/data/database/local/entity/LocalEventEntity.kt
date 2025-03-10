package com.radlance.eventum.data.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "event",
    foreignKeys = [
        ForeignKey(
            entity = LocalCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"]
        )
    ]
)
data class LocalEventEntity(
    val title: String,
    val description: String,
    @ColumnInfo("spend_time") val spendTime: String,
    @ColumnInfo("image_url") val imageUrl: String,
    @ColumnInfo("category_id", index = true) val categoryId: Int,
    @ColumnInfo("is_favorite") val isFavorite: Boolean = false,
    @PrimaryKey val id: Int = 0
)
