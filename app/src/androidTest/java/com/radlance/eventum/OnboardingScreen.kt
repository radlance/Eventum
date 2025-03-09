package com.radlance.eventum

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.radlance.eventum.navigation.base.Destination

data class OnboardingScreen(
    @StringRes val stringResId: Int,
    @DrawableRes val imageContentDescription: Int,
    val destination: Destination
)
