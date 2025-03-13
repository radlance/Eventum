package com.radlance.eventum.presentation.profile.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.radlance.eventum.R
import com.radlance.eventum.ui.theme.EventumTheme

@Composable
fun EditProfileImage(
    currentImage: Any,
    onContentImageChanged: (Uri) -> Unit,
    onPreviewImageChanged: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPickImageTypeDialog by remember { mutableStateOf(false) }

    val previewLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let { onPreviewImageChanged(it) }
    }

    val contentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onContentImageChanged(it) }
    }

    if (showPickImageTypeDialog) {
        Dialog(
            onDismissRequest = { showPickImageTypeDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            PickImageTypeDialog(
                onPhotoButtonClick = {
                    previewLauncher.launch()
                    showPickImageTypeDialog = false
                },
                onGalleryButtonClick = {
                    contentLauncher.launch("image/*")
                    showPickImageTypeDialog = false
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }

    Box(
        modifier = modifier
            .size(98.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(currentImage)
                .crossfade(true)
                .build(),

            contentDescription = stringResource(R.string.profile_image),
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .clickable { showPickImageTypeDialog = true },
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(R.drawable.ic_edit_profile_image),
            contentDescription = "ic_edit_profile_image",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-10).dp, y = (-2).dp)
                .clickable { showPickImageTypeDialog = true }
        )
    }
}

@Preview
@Composable
private fun EditProfileImagePreview() {
    EventumTheme {
        EditProfileImage(
            onContentImageChanged = {},
            currentImage = Uri.EMPTY,
            onPreviewImageChanged = {}
        )
    }
}