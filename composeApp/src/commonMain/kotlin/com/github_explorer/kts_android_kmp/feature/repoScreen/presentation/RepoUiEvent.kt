package com.github_explorer.kts_android_kmp.feature.repoScreen.presentation

sealed interface RepoUiEvent {
    data class Init(val owner: String, val repo: String) : RepoUiEvent

    data object Refresh : RepoUiEvent

    data object ToggleFavorite : RepoUiEvent

    data object RetryLoadDetails : RepoUiEvent

    data object RetryLoadReadme : RepoUiEvent

    data object ShareRepo : RepoUiEvent

    data object CreateIssue : RepoUiEvent
}
