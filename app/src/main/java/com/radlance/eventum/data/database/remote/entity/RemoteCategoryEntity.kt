package com.radlance.eventum.data.database.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class RemoteCategoryEntity(
    val id: Int,
    val title: String
)
