package com.radlance.eventum.data.database.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteEventEntity(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("category_id") val categoryId: Int,
    @SerialName("spend_time") val spendTime: String
)
