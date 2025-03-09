package com.radlance.eventum.presentation.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.R
import com.radlance.eventum.presentation.component.NavigationButton
import com.radlance.eventum.ui.theme.backgroundGradient
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun OnboardingSecond(
    onNextClicked: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackPressed() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (dragAmount > 10f) {
                        onBackPressed()
                    } else if (dragAmount < -10f) {
                        onNextClicked()
                    }
                    change.consume()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(80.dp))
            Box {
                Image(
                    painter = painterResource(R.drawable.onboarding_image_2),
                    contentDescription = stringResource(R.string.onboarding_image_2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(0.8f)
                        .padding(start = 36.dp, end = 20.dp, top = 10.dp),
                    contentScale = ContentScale.FillWidth
                )

                Image(
                    painter = painterResource(R.drawable.onboarding_highlight_3),
                    contentDescription = "onboarding_highlight_4",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .alpha(0.7f)
                        .offset(y = 12.dp)
                        .padding(end = 26.dp)
                )

                Image(
                    painter = painterResource(R.drawable.onboarding_highlight_4),
                    contentDescription = "onboarding_highlight_4",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .alpha(0.7f)
                        .offset(y = (-30).dp)
                        .rotate(70f)
                        .padding(start = 27.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.will_start_travel),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 34.sp,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 44.2.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.exhibitions_master_classes),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(37.5.dp))
            Image(
                painter = painterResource(R.drawable.onboarding_progress_2),
                contentDescription = "onboarding_progress_2"
            )
        }
        NavigationButton(
            stringResId = R.string.next,
            onClick = onNextClicked,
            buttonColors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 36.dp, top = 16.dp)
        )
    }
}

@Preview
@Composable
private fun OnboardingSecondPreview() {
    OnboardingSecond({}, {})
}

@Preview(device = "spec:width=673dp,height=841dp")
@Composable
private fun OnboardingSecondPreviewExpanded() {
    OnboardingSecond({}, {})
}