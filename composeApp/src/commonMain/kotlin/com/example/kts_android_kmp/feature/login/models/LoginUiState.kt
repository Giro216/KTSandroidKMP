package com.example.kts_android_kmp.feature.login.models

import androidx.compose.runtime.Immutable

@Immutable
data class LoginUiState(
    val username: String,
    val password: String,
    val passwordVisible: Boolean = false,
    val isLoginButtonActive: Boolean = false,
    val error: Boolean = false,
) {
    companion object {
        val Initial = LoginUiState(
            username = "",
            password = "",
            isLoginButtonActive = false,
            error = false,
        )
    }
}