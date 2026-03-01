package com.example.kts_android_kmp.feature.login

import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.feature.login.models.LoginUiEvent
import com.example.kts_android_kmp.feature.login.models.LoginUiState

class LoginViewModel : BaseViewModel<LoginUiEvent.LoginSuccessEvent, LoginUiState>(
    LoginUiState.Initial
) {

    fun onUsernameChanged(value: String) {
        updateState {
            val newHasError = validateEmail(value) != null || validatePassword(password) != null

            copy(
                username = value,
                error = newHasError,
                isLoginButtonActive = !newHasError,
            )
        }
    }

    fun onPasswordChanged(value: String) {
        updateState {
            val newHasError = validateEmail(username) != null || validatePassword(value) != null

            copy(
                password = value,
                error = newHasError,
                isLoginButtonActive = !newHasError,
            )
        }
    }

    fun onPasswordVisibilityToggled() {
        updateState { copy(passwordVisible = !passwordVisible) }
    }
}