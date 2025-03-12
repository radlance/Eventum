package com.radlance.eventum.navigation.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.radlance.eventum.R
import com.radlance.eventum.domain.user.User
import com.radlance.eventum.navigation.bottom.BottomNavigationState
import com.radlance.eventum.navigation.bottom.Favorite
import com.radlance.eventum.navigation.bottom.rememberNavigationState
import com.radlance.eventum.presentation.authorization.common.AuthResultUiState
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.componentGrayColor
import com.radlance.eventum.ui.theme.ralewayFamily
import com.radlance.eventum.ui.vector.CartIcon
import com.radlance.eventum.ui.vector.ExitIcon
import com.radlance.eventum.ui.vector.FavoriteOutlinedIcon
import com.radlance.eventum.ui.vector.NotificationNavigationIcon
import com.radlance.eventum.ui.vector.OrdersIcon
import com.radlance.eventum.ui.vector.ProfileNavigationIcon

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DrawerMenu(
    user: User,
    navigationState: BottomNavigationState,
    signOutState: AuthResultUiState,
    onMenuItemClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onSignOut: () -> Unit,
    navigateToCart: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToNotification: () -> Unit,
    navigateToOrderHistory: () -> Unit,
    notificationExist: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.surfaceTint,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(14.dp),
                )
            }
        },
    ) {
        signOutState.Show(
            onSuccessResult = onSignOut,
            onChangeButtonState = {},
            snackBarHostState = snackBarHostState
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(78.dp))

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(user.imageUrl)
                    .crossfade(true)
                    .build(),

                contentDescription = stringResource(R.string.profile_image),
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .clickable {
                        navigateToProfile()
                        onMenuItemClick()
                    },
                contentScale = ContentScale.Crop

            )

            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = user.name,
                fontSize = 20.sp,
                fontFamily = ralewayFamily,
                fontWeight = FontWeight.Bold,
                lineHeight = 23.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(55.dp))

            Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
                MenuItem(
                    icon = ProfileNavigationIcon(Color.White),
                    contentDescription = "ProfileNavigationIcon",
                    sectionResId = R.string.profile,
                    onItemClick = {
                        navigateToProfile()
                        onMenuItemClick()
                    }
                )

                MenuItem(
                    icon = CartIcon,
                    contentDescription = "CartIcon",
                    sectionResId = R.string.cart,
                    onItemClick = {
                        navigateToCart()
                        onMenuItemClick()
                    }
                )

                MenuItem(
                    icon = FavoriteOutlinedIcon(Color.White),
                    contentDescription = "CartIcon",
                    sectionResId = R.string.favorite,
                    onItemClick = {
                        navigationState.navigateTo(Favorite)
                        onMenuItemClick()
                    }
                )

                MenuItem(
                    icon = OrdersIcon,
                    contentDescription = "OrdersIcon",
                    sectionResId = R.string.orders,
                    iconOffset = IntOffset(x = 0, y = 15),
                    onItemClick = {
                        navigateToOrderHistory()
                        onMenuItemClick()
                    }
                )

                MenuItem(
                    icon = NotificationNavigationIcon(Color.White),
                    contentDescription = "NotificationIcon",
                    sectionResId = R.string.notifications,
                    onItemClick = {
                        navigateToNotification()
                        onMenuItemClick()
                    },
                    showBadge = notificationExist
                )
            }

            Spacer(Modifier.height(38.dp))
            HorizontalDivider(color = componentGrayColor, modifier = Modifier.alpha(0.23f))
            Spacer(Modifier.height(30.dp))

            MenuItem(
                icon = ExitIcon,
                contentDescription = "ExitIcon",
                sectionResId = R.string.log_out,
                onItemClick = onSignOutClick
            )
        }
    }

}


@Preview
@Composable
private fun DrawerMenuPreview() {
    EventumTheme {
        DrawerMenu(
            user = User(name = "stub"),
            navigationState = rememberNavigationState(),
            onMenuItemClick = {},
            onSignOutClick = {},
            onSignOut = {},
            signOutState = AuthResultUiState.Initial,
            notificationExist = false,
            navigateToCart = {},
            navigateToProfile = {},
            navigateToNotification = {},
            navigateToOrderHistory = {}
        )
    }
}

@Preview
@Composable
private fun MenuItemPreview() {
    EventumTheme {
        MenuItem(
            icon = NotificationNavigationIcon(Color.White),
            contentDescription = null,
            R.string.retry,
            showBadge = true,
            onItemClick = {}
        )
    }
}