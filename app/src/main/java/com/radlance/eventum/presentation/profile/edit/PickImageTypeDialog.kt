package com.radlance.eventum.presentation.profile.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.R
import com.radlance.eventum.presentation.component.NavigationButton
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.poppinsFamily

@Composable
fun PickImageTypeDialog(
    onPhotoButtonClick: () -> Unit,
    onGalleryButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(R.string.pick_image),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                lineHeight = 24.sp
            )

            Column {
                Row {
                    NavigationButton(
                        stringResId = R.string.camera,
                        onClick = onPhotoButtonClick,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .height(60.dp)
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    NavigationButton(
                        stringResId = R.string.gallery,
                        onClick = onGalleryButtonClick,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .height(60.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PickImageTypeDialogPreview() {
    EventumTheme {
        PickImageTypeDialog({}, {})
    }
}