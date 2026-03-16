package com.example.kts_android_kmp.feature.main.models

sealed interface MainUiEvent {
    data object ErrorLoadingRepos : MainUiEvent
    data object ReposLoaded : MainUiEvent
}