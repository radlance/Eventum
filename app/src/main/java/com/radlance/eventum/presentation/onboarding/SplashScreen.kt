package com.radlance.eventum.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.radlance.eventum.ui.theme.backgroundGradientInverse
import com.radlance.eventum.ui.theme.ralewayFamily
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onDelayFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(500)
        onDelayFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundGradientInverse),
        contentAlignment = Alignment.Center
    ) {
        val text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = ralewayFamily, fontWeight = FontWeight.Bold)) {
                append("EVENTUM")
            }
        }

        Text(
            text = text,
            fontSize = 65.sp,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen(onDelayFinished = {})
}