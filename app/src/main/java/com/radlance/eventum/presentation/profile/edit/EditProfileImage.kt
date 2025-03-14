package com.radlance.eventum.presentation.profile.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    showPickImageTypeDialog: Boolean,
    onCloseDialog: () -> Unit,
    modifier: Modifier = Modifier
) {

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
            onDismissRequest = onCloseDialog,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            PickImageTypeDialog(
                onPhotoButtonClick = {
                    previewLauncher.launch()
                    onCloseDialog()
                },
                onGalleryButtonClick = {
                    contentLauncher.launch("image/*")
                    onCloseDialog()
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
                .clip(CircleShape),
            contentScale = ContentScale.Crop
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
            onPreviewImageChanged = {},
            onCloseDialog = {},
            showPickImageTypeDialog = true
        )
    }
}