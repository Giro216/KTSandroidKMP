package com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.presentation

import androidx.compose.runtime.Immutable
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo

@Immutable
data class UserRepoUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,

    val repos: List<GitHubRepo> = emptyList(),
)
