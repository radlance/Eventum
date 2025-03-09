package com.radlance.eventum.domain.event

data class Event(
    val title: String,
    val description: String,
    val imageUrl: String,
    val categoryId: Int,
    val isFavorite: Boolean,
    val quantityInCart: Int,
    val pricesWithCategories: List<PriceWithCategory>,
    val spendTime: String,
    val id: Int = 0
)

data class PriceWithCategory(
    val id: Int,
    val title: String,
    val price: Double
)