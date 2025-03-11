package com.radlance.eventum.presentation.home.core

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.radlance.eventum.R
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.presentation.home.common.CategoriesRow
import com.radlance.eventum.presentation.home.common.ChangeEventStatus
import com.radlance.eventum.ui.theme.EventumTheme
import com.radlance.eventum.ui.theme.ralewayFamily

@Composable
fun HomeScreen(
    onBackPressed: () -> Unit,
    navigateToDetails: (Int) -> Unit,
    navigateToCatalog: (Int?) -> Unit,
    navigateToPopular: () -> Unit,
    onMenuIconClick: () -> Unit,
    onSearchFieldClick: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState { 3 }
    val scrollState = rememberScrollState()

    val loadContentResult by viewModel.catalogContent.collectAsState()
    val addToFavoriteResult by viewModel.favoriteResult.collectAsState()
    val addToCartResult by viewModel.inCartResult.collectAsState()

    BackHandler { onBackPressed() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(dimensionResource(R.dimen.main_top_padding)))
        HomeHeader(onMenuClick = onMenuIconClick, onCartClick = onCartClick)

        Spacer(Modifier.height(19.dp))

        HomeSearchBar(
            onSettingsClick = {},
            hint = stringResource(R.string.search),
            onSearchFieldClick = onSearchFieldClick,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(24.dp))

        addToFavoriteResult.Show(
            onSuccess = {},
            onLoading = { eventId ->
                ChangeEventStatus(eventId, viewModel::changeStateFavoriteStatus)
            },
            onError = { eventId ->
                ChangeEventStatus(eventId, viewModel::changeStateFavoriteStatus)
            },
            onUnauthorized = {}
        )

        addToCartResult.Show(
            onSuccess = {},
            onLoading = { event ->
                ChangeEventStatus(event, viewModel::changeStateInCartStatus)
            },
            onError = { eventId ->
                ChangeEventStatus(
                    eventId = eventId,
                    onStatusChanged = { viewModel.changeStateInCartStatus(it, recover = true) }
                )
            },
            onUnauthorized = {}
        )

        loadContentResult.Show(
            onSuccess = { fetchContent ->
                Log.d("HomeScreen", fetchContent.events.toString())
                CategoriesRow(
                    categories = fetchContent.categories,
                    onCategoryClick = navigateToCatalog,
                    selectedCategoryId = -1
                )

                Spacer(Modifier.height(24.dp))
                PopularRow(
                    events = fetchContent.events,
                    onLikeClick = viewModel::changeFavoriteStatus,
                    onCardClick = navigateToDetails,
                    navigateToPopular = navigateToPopular
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.for_you),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 18.dp)
                )

                Spacer(Modifier.height(25.dp))

                HorizontalPager(pagerState) { position ->
                    when (position) {
                        0 -> SaleBanner(
                            imageResId = R.drawable.suggestion_1,
                            onImageClick = { navigateToDetails(6) }
                        )

                        1 -> SaleBanner(
                            imageResId = R.drawable.suggestion_2,
                            onImageClick = { navigateToDetails(12) }
                        )

                        2 -> SaleBanner(
                            imageResId = R.drawable.suggestion_3,
                            onImageClick = { navigateToDetails(1) }
                        )
                    }
                }
            },

            onLoading = { CircularProgressIndicator() },
            onError = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(R.string.load_error))
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = viewModel::fetchContent) {
                            Text(stringResource(R.string.retry), color = Color.White)
                        }
                    }
                }
            },
            onUnauthorized = {}
        )


    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    EventumTheme(darkTheme = false) {
        HomeScreen({}, {}, {}, {}, {}, {}, { DrawerValue.Closed })
    }
}

@Preview(showBackground = true, device = "spec:width=673dp,height=841dp")
@Composable
private fun HomeScreenExpandedPreview() {
    EventumTheme(darkTheme = false) {
        HomeScreen({}, {}, {}, {}, {}, {}, { DrawerValue.Closed })
    }
}