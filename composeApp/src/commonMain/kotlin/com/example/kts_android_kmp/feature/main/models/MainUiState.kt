package com.example.kts_android_kmp.feature.main.models

import androidx.compose.runtime.Immutable

@Immutable
data class MainUiState(
    val query: String = "",
    val repos: List<GitHubRepoEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: Boolean = false,
)