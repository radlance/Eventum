package com.radlance.eventum.presentation.order

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.radlance.eventum.domain.user.User
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun OrderCard(
    user: User,
    onMapClick: (lat: Double, long: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .animateContentSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ContactIInformation(
                user = user,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.address),
                fontSize = 14.sp,
                fontFamily = ralewayFamily,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 20.dp, end = 32.dp)
            )

            OrderAddress(
                onMapClick = { onMapClick(59.922025, 30.355520) },
                modifier = Modifier.padding(start = 20.dp, end = 32.dp)
            )

            Spacer(Modifier.height(12.dp))

            PaymentMethod(
                cardName = "Dbl Card",
                cardNumber = "1234 5678 0696 4629",
                modifier = Modifier.padding(start = 20.dp, end = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun OrderCardPreview() {
    EventumTheme {
        OrderCard(
            user = User(),
            onMapClick = { _, _ -> },
            modifier = Modifier
                .height(425.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(device = "spec:width=673dp,height=841dp")
@Composable
private fun OrderCardExpandedPreview() {
    EventumTheme {
        OrderCard(
            user = User(),
            onMapClick = { _, _ -> },
            modifier = Modifier
                .height(425.dp)
                .fillMaxWidth()
        )
    }
}