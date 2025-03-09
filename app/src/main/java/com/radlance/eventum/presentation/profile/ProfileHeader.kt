package com.radlance.eventum.presentation.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Composable
fun ProfileHeader(
    onBackPressed : () -> Unit,
    modifier: Modifier = Modifier
) {
    CommonHeader(
        startContent = {
            BackButton(
                onClick = onBackPressed,
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant
            )
        },
        middleContent = {
            Text(
                text = stringResource(R.string.profile),
                fontSize = 16.sp,
                fontFamily = ralewayFamily,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )
        },
        endContent = {
            Text(
                text = stringResource(R.string.done),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontFamily = ralewayFamily,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp
            )
        },

        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 21.dp)
    )
}

@Preview
@Composable
private fun ProfileHeaderPreview() {
    EventumTheme {
        ProfileHeader({})
    }
}