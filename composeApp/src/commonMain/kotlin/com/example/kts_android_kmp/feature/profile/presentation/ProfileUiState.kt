package com.example.kts_android_kmp.feature.profile.presentation

import androidx.compose.runtime.Immutable
import com.example.kts_android_kmp.feature.profile.domain.UserProfileEntity

@Immutable
data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfileEntity? = null,
    val isError: Boolean = false,
)
