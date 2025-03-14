package com.radlance.eventum.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.radlance.eventum.R
import com.radlance.eventum.presentation.authorization.common.AuthScaffold
import com.radlance.eventum.presentation.profile.edit.ImageState
import com.radlance.eventum.ui.theme.EventumTheme

@Composable
fun ProfileScreen(
    onBarcodeClick: () -> Unit,
    onSignInClick: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val userData by profileViewModel.userData.collectAsState()
    var imageState by remember { mutableStateOf<ImageState>(ImageState.Base) }
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var nameFieldValue by rememberSaveable { mutableStateOf("") }
    var lastNameFieldValue by rememberSaveable { mutableStateOf("") }
    var addressFieldValue by rememberSaveable { mutableStateOf("") }
    var phoneFieldValue by rememberSaveable { mutableStateOf("") }

    val updateUserResult by profileViewModel.updateUserResult.collectAsState()

    updateUserResult.Show(
        onSuccess = {
            LaunchedEffect(Unit) {
                snackBarHostState.showSnackbar(
                    message = context.getString(R.string.change_profile_sucess),
                    duration = SnackbarDuration.Short
                )
            }
        },

        onError = {
            LaunchedEffect(Unit) {
                snackBarHostState.showSnackbar(
                    message = context.getString(R.string.fail_data_save),
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true
                )
            }
        },

        onLoading = {
            LaunchedEffect(Unit) {
                keyboardController?.hide()
                snackBarHostState.showSnackbar(
                    message = context.getString(R.string.save_data),
                    duration = SnackbarDuration.Indefinite
                )
            }
        },
        onUnauthorized = {}
    )

    AuthScaffold(
        snackBarHostState = snackBarHostState
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(dimensionResource(R.dimen.main_top_padding)))

            userData.Show(
                onSuccess = { userData ->

                    ProfileHeader(
                        onBackPressed = onBackPressed,
                        onDoneClick = {
                            val byteArray = if (imageState.image() != userData.imageUrl.toUri()) {
                                imageState.byteArray(context)
                            } else {
                                null
                            }

                            profileViewModel.saveProfileChanges(
                                firstName = nameFieldValue,
                                lastName = lastNameFieldValue,
                                address = addressFieldValue,
                                phoneNumber = phoneFieldValue,
                                imageByteArray = byteArray
                            )
                        }
                    )

                    Spacer(Modifier.height(24.dp))

                    LaunchedEffect(userData.imageUrl) {
                        if (userData.imageUrl.isNotEmpty()) {
                            imageState = ImageState.UriImage(userData.imageUrl.toUri())
                        }
                    }

                    LaunchedEffect(userData.firstName) { nameFieldValue = userData.firstName }

                    LaunchedEffect(userData.lastName) { lastNameFieldValue = userData.lastName }

                    LaunchedEffect(userData.address) { addressFieldValue = userData.address }

                    LaunchedEffect(userData.phoneNumber) { phoneFieldValue = userData.phoneNumber }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfilePictureSection(
                            currentImage = imageState.image(),
                            onContentImageChanged = { imageState = ImageState.UriImage(it) },
                            onPreviewImageChanged = { imageState = ImageState.BitmapImage(it) },
                            name = userData.firstName,
                            imageUrl = userData.imageUrl
                        )

                        Spacer(Modifier.height(19.dp))
                        Barcode(
                            onBarcodeClick = onBarcodeClick,
                            modifier = Modifier.padding(start = 15.dp, end = 20.dp)
                        )
                        ProfileDataColumn(
                            nameFieldValue = nameFieldValue,
                            onNameValueChange = { nameFieldValue = it },
                            lastNameFieldValue = lastNameFieldValue,
                            onLastNameValueChange = { lastNameFieldValue = it },
                            addressFieldValue = addressFieldValue,
                            onAddressValueChange = { addressFieldValue = it },
                            phoneNumberFieldValue = phoneFieldValue,
                            onPhoneNumberValueChange = { phoneFieldValue = it },
                            modifier = Modifier
                                .padding(start = 22.dp, end = 18.dp)
                                .safeDrawingPadding()
                                .offset(y = (-20).dp)
                        )
                    }
                },
                onError = {},
                onLoading = {},
                onUnauthorized = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            modifier = Modifier.offset(y = (-55).dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(onClick = onSignInClick) {
                                Text(text = stringResource(R.string.sign_in))
                            }
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    EventumTheme {
        ProfileScreen({}, {}, {})
    }
}

@Preview(device = "spec:width=673dp,height=841dp")
@Composable
private fun ProfileScreenPreviewExpanded() {
    EventumTheme {
        ProfileScreen({}, {}, {})
    }
}