package com.example.kts_android_kmp.feature.login.oauth

import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.feature.login.oauth.model.LoginState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val appAuthHandler: AppAuthHandler,
) : BaseViewModel<Nothing, LoginState>(
    LoginState(isLoggedIn = TokenStorage.accessToken != null)
) {

    fun openLoginPage() : Boolean {

        viewModelScope.launch {
            runCatching {
                appAuthHandler.performTokenRequest()
            }.onSuccess { tokens ->
                if (tokens != null) {
                    authRepository.saveTokens(tokens)
                    updateState { copy(isLoggedIn = true) }
                }
            }.onFailure {
                Napier.e("Failed to perform token request: ${it.message}", throwable = it)
            }
        }

        return state.value.isLoggedIn
    }
}

