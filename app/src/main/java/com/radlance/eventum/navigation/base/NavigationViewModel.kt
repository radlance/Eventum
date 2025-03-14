package com.radlance.eventum.navigation.base

import androidx.lifecycle.viewModelScope
import com.radlance.eventum.domain.onboarding.NavigationRepository
import com.radlance.eventum.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository
) : BaseViewModel() {
    private val onBoardingAlreadyViewed = navigationRepository.getOnBoardingViewingStatus()
    private val userAlreadyLoggedIn = navigationRepository.getLoggedInStatus()

    val navigationState: StateFlow<OnBoardingNavigationState> =
        combine(onBoardingAlreadyViewed, userAlreadyLoggedIn) { onBoardingViewed, userLoggedIn ->
            when {
                onBoardingViewed && userLoggedIn -> OnBoardingNavigationState.NavigateToHome
                onBoardingViewed && !userLoggedIn -> OnBoardingNavigationState.NavigateToSignIn
                else -> OnBoardingNavigationState.NavigateToOnBoardingFirst
            }
        }.stateInViewModel(
            initialValue = OnBoardingNavigationState.NavigateToOnBoardingFirst
        )

    fun setOnBoardingViewed(viewed: Boolean) {
        viewModelScope.launch {
            navigationRepository.setOnBoardingViewed(viewed)
        }
    }

    fun setUserLoggedIn() {
        viewModelScope.launch {
            navigationRepository.setUserLoggedIn(true)
        }
    }
}