package com.radlance.eventum.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.radlance.eventum.R
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.presentation.home.common.ChangeEventStatus
import com.radlance.eventum.presentation.profile.ProfileViewModel
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun CartScreen(
    onPlaceOrderClick: () -> Unit,
    onBackPressed: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userData by profileViewModel.userData.collectAsState()

    val cartContent by eventViewModel.cartContent.collectAsState()
    val quantityResult by eventViewModel.quantityResult.collectAsState()
    val removeResult by eventViewModel.removeResult.collectAsState()

    var incrementCurrent by rememberSaveable { mutableStateOf(false) }
    var observeUserData by rememberSaveable { mutableStateOf(false) }


    if (observeUserData) {
        userData.Show(
            onSuccess = {
                LaunchedEffect(Unit) {
                    onPlaceOrderClick()
                    observeUserData = false
                }
            },
            onError = {},
            onLoading = {},
            onUnauthorized = {
                Dialog(
                    onDismissRequest = {
                        observeUserData = false
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    UnauthorizedDialog(
                        onSignInClick = {
                            onSignInClick()
                            observeUserData = false
                        },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        )
    }

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(dimensionResource(R.dimen.main_top_padding)))
            CartHeader(onBackPressed = onBackPressed)
            Spacer(Modifier.height(16.dp))

            removeResult.Show(
                onSuccess = {},
                onError = { eventId ->
                    ChangeEventStatus(
                        eventId = eventId,
                        onStatusChanged = {
                            eventViewModel.deleteCartItemFromCurrentState(
                                eventPriceId = it,
                                recover = true
                            )
                        }
                    )
                },
                onLoading = { eventId ->
                    ChangeEventStatus(
                        eventId = eventId,
                        onStatusChanged = eventViewModel::deleteCartItemFromCurrentState
                    )
                },
                onUnauthorized = {}
            )

            quantityResult.Show(
                onSuccess = {},
                onError = { eventId ->
                    ChangeEventStatus(eventId) {
                        eventViewModel.updateCurrentQuantity(it, !incrementCurrent)
                    }
                },
                onLoading = { eventId ->
                    ChangeEventStatus(eventId) {
                        eventViewModel.updateCurrentQuantity(it, incrementCurrent)
                    }
                },
                onUnauthorized = {}
            )

            cartContent.Show(
                onSuccess = { eventsInCart ->
                    val filteredEvents = eventsInCart.filter { it.quantity != 0 }
                    if (filteredEvents.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(R.string.cart_is_empty),
                                fontSize = 16.sp,
                                fontFamily = ralewayFamily,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 20.sp,
                                modifier = Modifier.offset(y = (-55).dp)
                            )
                        }
                    } else {
                        CartEventColumn(
                            cartItems = filteredEvents,
                            onChangeQuantityClick = { eventPriceId, quantity, increment ->
                                eventViewModel.updateEventQuantity(eventPriceId, quantity)
                                incrementCurrent = increment
                            },

                            onRemoveEvent = eventViewModel::removeEventFromCart,

                            modifier = Modifier.weight(4.7f)
                        )
                        Box(
                            modifier = Modifier.weight(2f)
                        ) {
                            CartResult(
                                eventsPrice = eventsInCart.sumOf { it.price * it.quantity },
                                buttonStringResId = R.string.place_order,
                                onButtonClick = {
                                    observeUserData = true
                                }
                            )
                        }
                    }

                },
                onError = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .offset(y = (-55).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(R.string.load_error))
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = eventViewModel::fetchCartContent) {
                                Text(stringResource(R.string.retry), color = Color.White)
                            }
                        }
                    }
                },
                onLoading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.offset(y = (-55).dp))
                    }
                },
                onUnauthorized = {}
            )
        }
    }
}

@Preview
@Composable
private fun CartScreenPreview() {
    EventumTheme {
        CartScreen({}, {}, {})
    }
}