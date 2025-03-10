package com.radlance.eventum.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radlance.eventum.R
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.ui.theme.EventumTheme

@Composable
fun HistoryScreen(
    onSignInClick: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = hiltViewModel()
) {
    val fetchHistoryResult by viewModel.historyResult.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Spacer(Modifier.height(dimensionResource(R.dimen.main_top_padding)))
        HistoryHeader(onBackPressed = onBackPressed)
        Spacer(Modifier.height(16.dp))

        fetchHistoryResult.Show(
            onSuccess = { history ->
                if (history.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.no_orders),
                            modifier = Modifier.offset(y = (-55).dp)
                        )
                    }
                } else {
                    HistoryList(history)
                }
            },

            onLoading = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.offset(y = (-55).dp))
                }
            },
            onError = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.offset(y = (-55).dp)
                    ) {
                        Text(text = stringResource(R.string.load_error))
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = viewModel::fetchHistory) {
                            Text(text = stringResource(R.string.retry), color = Color.White)
                        }
                    }
                }

            },
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

@Preview
@Composable
private fun HistoryScreenPreview() {
    EventumTheme {
        HistoryScreen({}, {})
    }
}