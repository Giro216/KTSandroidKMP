package com.example.kts_android_kmp.feature.main.models

import androidx.compose.runtime.Immutable

@Immutable
data class MainUiState(
    val query: String = "",
    val repos: List<GitHubRepoEntity> = emptyList(),
    val totalCount: Int? = null,

    val isLoading: Boolean = true,
    val isInitialError: Boolean = false,

    val page: Int = 1,
    val isPaginationLoading: Boolean = false,
    val canPaginate: Boolean = true,
    val isPaginationError: Boolean = false,
)