package com.radlance.eventum.presentation.order

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.radlance.eventum.R
import com.radlance.eventum.domain.user.User
import com.radlance.eventum.presentation.cart.CartResult
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.ui.theme.EventumTheme

@Composable
fun OrderScreen(
    onBackPressed: () -> Unit,
    navigateToCatalog: () -> Unit,
    onMapClick: (lat: Double, long: Double) -> Unit,
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    var resultSum by rememberSaveable { mutableDoubleStateOf(0.0) }
    var currentUser by remember { mutableStateOf(User()) }

    val cartContent by eventViewModel.cartContent.collectAsState()
    val placeOrderResult by eventViewModel.placeOrderResult.collectAsState()
    val userUiState by orderViewModel.userUiState.collectAsState()

    val context = LocalContext.current

    var placeOrderButtonEnabled by rememberSaveable { mutableStateOf(true) }
    var showSuccessOrderPlaceDialog by rememberSaveable { mutableStateOf(false) }

    userUiState.Show(
        onSuccess = { userData ->
            currentUser = userData
        },
        onError = {},
        onLoading = {},
        onUnauthorized = {}
    )

    if (showSuccessOrderPlaceDialog) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            SuccessOrderPlaceDialog(
                navigateToCatalog = {
                    navigateToCatalog()
                    showSuccessOrderPlaceDialog = false
                    eventViewModel.resetPlaceOrderResult()
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(Modifier.height(dimensionResource(R.dimen.main_top_padding)))
            OrderHeader(onBackPressed = onBackPressed)
            Spacer(Modifier.height(46.dp))

            cartContent.Show(
                onSuccess = { cartList ->
                    Column(modifier = Modifier.weight(1f)) {
                        OrderCard(
                            email = currentUser.email,
                            onMapClick = onMapClick,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                    }
                    Box {
                        if (resultSum == 0.0) {
                            resultSum = cartList.sumOf { it.price * it.quantity }
                        }
                        CartResult(
                            modifier = Modifier.wrapContentSize(),
                            eventsPrice = resultSum,
                            buttonStringResId = R.string.confirm,
                            buttonEnabled = placeOrderButtonEnabled,
                            onButtonClick = { eventViewModel.placeOrder(cartList) },
                            innerBottomPadding = paddingValues.calculateBottomPadding()
                        )
                    }
                },
                onError = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(R.string.load_error))
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = eventViewModel::fetchContent) {
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

            placeOrderResult.Show(
                onSuccess = {
                    LaunchedEffect(Unit) {
                        showSuccessOrderPlaceDialog = true
                        eventViewModel.updateStateAfterPlaceOrder(it)
                    }
                },
                onLoading = { placeOrderButtonEnabled = false },
                onError = {
                    placeOrderButtonEnabled = true
                    Toast.makeText(
                        context,
                        stringResource(R.string.place_order_error),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onUnauthorized = {}
            )
        }
    }
}

@Preview
@Composable
private fun OrderScreenPreview() {
    EventumTheme {
        OrderScreen(onBackPressed = {}, navigateToCatalog = {}, onMapClick = { _, _ -> })
    }
}

@Preview(device = "id:Nexus 5X")
@Composable
private fun OrderScreenSmallPreview() {
    EventumTheme {
        OrderScreen(onBackPressed = {}, navigateToCatalog = {}, onMapClick = { _, _ -> })
    }
}
