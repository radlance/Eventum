package com.radlance.eventum.presentation.order

import com.radlance.eventum.domain.user.User
import com.radlance.eventum.domain.user.UserRepository
import com.radlance.eventum.presentation.common.BaseViewModel
import com.radlance.eventum.presentation.common.FetchResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _userUiState = MutableStateFlow<FetchResultUiState<User>>(FetchResultUiState.Initial())
    val userUiState: StateFlow<FetchResultUiState<User>> = _userUiState.onStart {
        updateFetchUiState(_userUiState) {
            userRepository.getCurrentUserData()
        }
    }.stateInViewModel(FetchResultUiState.Initial())
}