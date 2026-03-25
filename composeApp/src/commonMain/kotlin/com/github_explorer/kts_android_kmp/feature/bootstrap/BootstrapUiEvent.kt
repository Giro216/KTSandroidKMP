package com.github_explorer.kts_android_kmp.feature.bootstrap

sealed interface BootstrapUiEvent {
    data object NavigateToHello : BootstrapUiEvent
    data object NavigateToMain : BootstrapUiEvent
}
