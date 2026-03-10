package com.example.kts_android_kmp.feature.main.models

import androidx.compose.runtime.Immutable

@Immutable
data class MainUiState(
    val query: String = "",
    val allRepos: List<GitHubRepo> = emptyList(),
    val filteredRepos: List<GitHubRepo> = emptyList(),
)