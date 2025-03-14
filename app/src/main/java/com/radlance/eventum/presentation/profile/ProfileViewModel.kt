package com.radlance.eventum.presentation.profile

import com.radlance.eventum.domain.user.User
import com.radlance.eventum.domain.user.UserRepository
import com.radlance.eventum.presentation.common.BaseViewModel
import com.radlance.eventum.presentation.common.FetchResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _userData = MutableStateFlow<FetchResultUiState<User>>(FetchResultUiState.Initial())
    val userData: StateFlow<FetchResultUiState<User>> = _userData.onStart {
        updateFetchUiState(_userData) {
            userRepository.getCurrentUserData()
        }
    }.stateInViewModel(FetchResultUiState.Initial())

    private val _updateUserResult =
        MutableStateFlow<FetchResultUiState<Unit>>(FetchResultUiState.Initial())
    val updateUserResult: StateFlow<FetchResultUiState<Unit>>
        get() = _updateUserResult.asStateFlow()

    fun saveProfileChanges(
        firstName: String,
        lastName: String,
        address: String,
        phoneNumber: String,
        imageByteArray: ByteArray?
    ) {
        val user = User(
            firstName = firstName,
            lastName = lastName,
            address = address,
            phoneNumber = phoneNumber
        )

        updateFetchUiState(_updateUserResult) {
            userRepository.updateUserData(user, imageByteArray)
        }
    }
}