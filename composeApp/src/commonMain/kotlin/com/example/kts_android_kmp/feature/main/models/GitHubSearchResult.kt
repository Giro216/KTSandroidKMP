package com.example.kts_android_kmp.feature.main.models

import androidx.compose.runtime.Immutable

@Immutable
data class GitHubSearchResult(
    val totalCount: Int,
    val items: List<GitHubRepoEntity>,
)

