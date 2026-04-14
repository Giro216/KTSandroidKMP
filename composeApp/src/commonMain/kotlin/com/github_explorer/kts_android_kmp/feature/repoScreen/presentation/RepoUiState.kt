package com.github_explorer.kts_android_kmp.feature.repoScreen.presentation

import androidx.compose.runtime.Immutable
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.GithubRepoDetails
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.GithubRepoReadme

@Immutable
data class RepoUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null,

    val owner: String? = null,
    val repo: String? = null,

    val details: GithubRepoDetails? = null,
    val isDetailsLoading: Boolean = false,

    val readme: GithubRepoReadme? = null,
    val isReadmeLoading: Boolean = false,

    val isStarredLocally: Boolean = false,
)
