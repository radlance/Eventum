package com.radlance.eventum.presentation.profile

import com.radlance.eventum.domain.user.User
import com.radlance.eventum.domain.user.UserRepository
import com.radlance.eventum.presentation.common.BaseViewModel
import com.radlance.eventum.presentation.common.FetchResultUiState
import com.radlance.eventum.presentation.profile.edit.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
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

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState>
        get() = _profileUiState.asStateFlow()

    fun saveProfileChanges(name: String, email: String, imageByteArray: ByteArray?) {
        validateFields(name, email)

        with(profileUiState.value) {
            if (isValidName && isValidEmail) {
                val user = User(name = name, email = email)
                updateFetchUiState(_updateUserResult) {
                    userRepository.updateUserData(user, imageByteArray)
                }
            }
        }
    }

    private fun validateFields(name: String, email: String) {
        _profileUiState.update { currentState ->
            currentState.copy(
                isValidName = name.isNotBlank(),
                isValidEmail = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}$").matches(email)
            )
        }
    }

    fun resetNameError() {
        _profileUiState.update { currentState ->
            currentState.copy(isValidName = true)
        }
    }

    fun resetEmailError() {
        _profileUiState.update { currentState ->
            currentState.copy(isValidEmail = true)
        }
    }

    fun updateActionButtonState(isEnabled: Boolean) {
        _profileUiState.update { currentState ->
            currentState.copy(isEnabledButton = isEnabled)
        }
    }
}