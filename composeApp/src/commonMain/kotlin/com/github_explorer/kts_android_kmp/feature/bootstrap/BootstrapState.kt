package com.github_explorer.kts_android_kmp.feature.bootstrap

data class BootstrapState(
    val isLoading: Boolean = true,
    val onboardingShown: Boolean = false,
    val isLoggedIn: Boolean = false,
)