package com.radlance.eventum.domain.user

data class User(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val imageUrl: String = ""
)
