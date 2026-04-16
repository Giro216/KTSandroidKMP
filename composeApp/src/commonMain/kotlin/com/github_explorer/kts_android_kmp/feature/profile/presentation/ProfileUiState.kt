package com.github_explorer.kts_android_kmp.feature.profile.presentation

import androidx.compose.runtime.Immutable
import com.github_explorer.kts_android_kmp.feature.profile.domain.UserProfile

@Immutable
data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val isError: Boolean = false,
)
