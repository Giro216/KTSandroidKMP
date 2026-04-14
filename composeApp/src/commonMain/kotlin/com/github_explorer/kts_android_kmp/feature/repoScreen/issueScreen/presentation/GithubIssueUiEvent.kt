package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation

sealed interface GithubIssueUiEvent {
    data class Init(val owner: String, val repo: String) : GithubIssueUiEvent

    data object RefreshLoadIssues : GithubIssueUiEvent

    data object OpenCreateIssueDialog : GithubIssueUiEvent
    data object DismissCreateIssueDialog : GithubIssueUiEvent
    data object DismissOwnershipWarning : GithubIssueUiEvent
    data object DismissCreateIssueSuccess : GithubIssueUiEvent
    data class IssueTitleChanged(val value: String) : GithubIssueUiEvent
    data class IssueBodyChanged(val value: String) : GithubIssueUiEvent
    data object CreateIssue : GithubIssueUiEvent
}
