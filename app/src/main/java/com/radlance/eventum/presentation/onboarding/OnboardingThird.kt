package com.radlance.eventum.presentation.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.R
import com.radlance.eventum.presentation.component.NavigationButton
import com.radlance.eventum.ui.theme.backgroundGradient
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun OnboardingThird(
    innerPaddingBottom: Dp,
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
            Spacer(Modifier.height(40.dp))
            Box {
                Image(
                    painter = painterResource(R.drawable.onboarding_image_3),
                    contentDescription = stringResource(R.string.onboarding_image_3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 26.dp)
                        .scale(0.7f),
                    contentScale = ContentScale.FillWidth
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-55).dp)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.choose_what_you_like),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 34.sp,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 44.2.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.many_exhibitions),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(37.5.dp))
                Image(
                    painter = painterResource(R.drawable.onboarding_progress_3),
                    contentDescription = "onboarding_progress_3"
                )
                Spacer(Modifier.height(20.dp))
            }
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
                .padding(start = 20.dp, end = 20.dp, bottom = 36.dp + innerPaddingBottom, top = 16.dp)
        )
    }
}

@Preview
@Composable
private fun OnboardingThirdScreenPreview() {
    OnboardingThird(0.dp, {}, {})
}


@Preview(device = "spec:width=673dp,height=841dp")
@Composable
private fun OnboardingThirdPreviewExpanded() {
    OnboardingThird(0.dp, {}, {})
}