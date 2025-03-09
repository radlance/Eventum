package com.radlance.eventum.presentation.home.popular

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.R
import com.radlance.eventum.presentation.component.BackButton
import com.radlance.eventum.presentation.component.CommonHeader
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily
import com.radlance.eventum.ui.vector.FavoriteOutlinedIcon

@Composable
fun PopularHeader(
    onBackPressed: () -> Unit,
    navigateToFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    CommonHeader(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 21.dp),

        startContent = {
            BackButton(
                onClick = onBackPressed,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant
            )
        },
        middleContent = {
            Text(
                text = stringResource(R.string.popular),
                fontSize = 16.sp,
                fontFamily = ralewayFamily,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )
        },
        endContent = {
            IconButton(
                onClick = navigateToFavorite,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Image(
                    imageVector = FavoriteOutlinedIcon(MaterialTheme.colorScheme.onSurface),
                    contentDescription = "LikeIcon"
                )
            }
        }
    )
}

@Preview
@Composable
private fun PopularHeaderPreview() {
    EventumTheme {
        PopularHeader({}, {})
    }
}