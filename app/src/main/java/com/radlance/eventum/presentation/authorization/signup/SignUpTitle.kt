package com.radlance.eventum.presentation.authorization.signup

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radlance.eventum.R
import com.radlance.eventum.presentation.component.BackButton
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.poppinsFamily
import com.radlance.eventum.ui.theme.ralewayFamily
import com.radlance.eventum.ui.theme.secondaryTextColor

@Composable
fun SignUpTitle(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackButton(onClick = onBackPressed, modifier = modifier)

    Text(
        text = stringResource(R.string.registration),
        fontSize = 32.sp,
        fontFamily = ralewayFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 11.dp)
    )
    Text(
        text = stringResource(R.string.fill_your_data),
        fontSize = 16.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = secondaryTextColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Preview
@Composable
private fun SignUpTitlePreview() {
    EventumTheme {
        SignUpTitle({})
    }
}