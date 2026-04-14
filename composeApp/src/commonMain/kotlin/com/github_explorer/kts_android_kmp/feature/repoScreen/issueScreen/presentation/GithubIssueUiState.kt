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
    val canCreateIssue: Boolean = false,
    val ownershipWarningMessage: String? = null,
    val createIssueSuccessMessage: String? = null,

    val issueList: List<GithubIssue> = emptyList(),

    val isCreateDialogOpen: Boolean = false,
    val issueTitleInput: String = "",
    val issueBodyInput: String = "",
    val isCreatingIssue: Boolean = false,
    val createIssueError: String? = null,
)
