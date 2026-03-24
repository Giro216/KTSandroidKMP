package com.example.kts_android_kmp.feature.login.oauth.presentation

import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.core.data.storage.domain.SessionRepository
import com.example.kts_android_kmp.feature.login.oauth.domain.AuthRepository
import com.example.kts_android_kmp.feature.login.oauth.platform.AppAuthHandler
import com.example.kts_android_kmp.utils.coRunCatching
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val appAuthHandler: AppAuthHandler,
    private val sessionRepository: SessionRepository,
) : BaseViewModel<Nothing, LoginState>(
    LoginState(isLoggedIn = false)
) {

    init {
        viewModelScope.launch {
            sessionRepository.isLoggedIn().collectLatest { loggedIn ->
                updateState { copy(isLoggedIn = loggedIn) }
            }
        }
    }

    fun openLoginPage() {
        viewModelScope.launch {
            coRunCatching {
                appAuthHandler.performTokenRequest()
            }.onSuccess { tokens ->
                if (tokens != null) {

                    Napier.i("tokens: $tokens", tag = "LoginViewModel")
                    sessionRepository.setOnboardingShown(true)
                        .onFailure { Napier.e("Failed to set onboarding shown: ${it.message}", it) }

                    authRepository.saveTokens(tokens)
                        .onFailure { Napier.e("Failed to save tokens: ${it.message}", it) }
                }
            }.onFailure {
                Napier.e("Failed to perform token request: ${it.message}", throwable = it)
            }
        }
    }
}