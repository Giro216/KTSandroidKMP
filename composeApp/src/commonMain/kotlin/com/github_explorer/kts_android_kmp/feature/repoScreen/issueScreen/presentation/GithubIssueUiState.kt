package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation

import androidx.compose.runtime.Immutable
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssue
import org.jetbrains.compose.resources.StringResource

sealed interface IssueMessage {
    data class Resource(val value: StringResource) : IssueMessage

    data class Dynamic(val value: String) : IssueMessage
}

@Immutable
data class GithubIssueUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: IssueMessage? = null,

    val owner: String? = null,
    val repo: String? = null,
    val canCreateIssue: Boolean = false,
    val isOwnershipWarning: Boolean = false,
    val createIssueSuccessMessage: IssueMessage? = null,

    val issueList: List<GithubIssue> = emptyList(),

    val isCreateDialogOpen: Boolean = false,
    val issueTitleInput: String = "",
    val issueBodyInput: String = "",
    val isCreatingIssue: Boolean = false,
    val createIssueError: IssueMessage? = null,
)
