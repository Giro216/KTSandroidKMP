package com.example.kts_android_kmp.feature.mainScreen.presentation

import androidx.compose.runtime.Immutable
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepo

@Immutable
data class MainUiState(
    val query: String = "",
    val repos: List<GitHubRepo> = emptyList(),
    val totalCount: Int? = null,

    val isLoading: Boolean = true,
    val isInitialError: Boolean = false,
    val hint: HintContent? = null,

    val pagination: PaginationUiState = PaginationUiState(),
) {

    data class PaginationUiState(
        val page: Int = 1,
        val isPaginationLoading: Boolean = false,
        val canPaginate: Boolean = true,
        val isPaginationError: Boolean = false,
    )
}

