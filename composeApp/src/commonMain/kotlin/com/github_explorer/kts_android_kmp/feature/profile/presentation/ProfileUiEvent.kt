package com.github_explorer.kts_android_kmp.feature.profile.presentation

sealed interface ProfileUiEvent {
    data object LogoutSuccess : ProfileUiEvent
}
