package com.example.kts_android_kmp.feature.mainScreen.presentation

sealed interface MainUiEvent {
    data class QueryChanged(val query: String) : MainUiEvent

    data object SearchClicked : MainUiEvent
    data object RetryClicked : MainUiEvent
    data object LoadNextPageRequested : MainUiEvent

    // UI events
    data object ReposLoaded : MainUiEvent
    data object ErrorLoadingRepos : MainUiEvent
}