package com.radlance.eventum.presentation.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.radlance.eventum.R
import com.radlance.eventum.presentation.profile.edit.EditProfileImage
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun ProfilePictureSection(
    currentImage: Any,
    onContentImageChanged: (Uri) -> Unit,
    onPreviewImageChanged: (Bitmap) -> Unit,
    name: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    var imageState by rememberSaveable { mutableStateOf<Uri>(Uri.EMPTY) }
    var showPickImageTypeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(imageUrl) {
        imageState = imageUrl.toUri()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        EditProfileImage(
            currentImage = currentImage,
            onContentImageChanged = onContentImageChanged,
            onPreviewImageChanged = onPreviewImageChanged,
            showPickImageTypeDialog = showPickImageTypeDialog,
            onCloseDialog = { showPickImageTypeDialog = false }
        )


        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 20.sp,
            fontFamily = ralewayFamily,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 23.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.edit_profile),
            fontSize = 12.sp,
            fontFamily = ralewayFamily,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp,
            modifier = Modifier.clickable { showPickImageTypeDialog = true }
        )
    }
}


@Preview
@Composable
private fun ProfilePictureSectionPreview() {
    EventumTheme {
        ProfilePictureSection(
            name = "name",
            imageUrl = "",
            currentImage = "",
            onContentImageChanged = {},
            onPreviewImageChanged = {}
        )
    }
}