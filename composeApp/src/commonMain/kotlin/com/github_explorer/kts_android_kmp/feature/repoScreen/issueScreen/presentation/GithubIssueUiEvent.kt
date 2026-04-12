package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation

sealed interface GithubIssueUiEvent {
    data class Init(val owner: String, val repo: String) : GithubIssueUiEvent

    data object RefreshLoadIssues : GithubIssueUiEvent

    data object CreateIssue : GithubIssueUiEvent
}
