package com.radlance.eventum.presentation.authorization.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthScaffold(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackBarPaddingValues: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit
) {

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.surfaceTint,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    dismissActionContentColor = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.padding(paddingValues = snackBarPaddingValues)
                )
            }
        },
        modifier = modifier
    ) {
        content()
    }
}