package com.radlance.eventum.common

import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes id: Int): String
}