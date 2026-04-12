package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation

import androidx.compose.runtime.Immutable
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssue

@Immutable
data class GithubIssueUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null,

    val owner: String? = null,
    val repo: String? = null,

    val issueList: ArrayList<GithubIssue> = arrayListOf(),
)
