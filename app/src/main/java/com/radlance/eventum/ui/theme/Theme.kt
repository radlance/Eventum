package com.radlance.eventum.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = redPrimaryColor,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = defaultDarkBackgroundColor,
    onSurface = Color.White,
    surfaceContainer = Color.White,
    surfaceVariant = componentGrayColorDark,
    surfaceTint = componentGrayColorDark
)

private val LightColorScheme = lightColorScheme(
    primary = redPrimaryColor,
    background = Color.White,
    secondary = PurpleGrey40,
    surface = lightThemeSurfaceColor,
    onSurface = Color.Black,
    surfaceContainer = Color.Black,
    surfaceVariant = Color.White,
    tertiary = Pink40,
    surfaceTint = componentGrayColor
)

@Composable
fun EventumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}