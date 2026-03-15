package com.example.kts_android_kmp.feature.mainScreen.domain

import androidx.compose.runtime.Immutable

@Immutable
data class GitHubRepoEntity(
    val id: Long,
    val owner: String,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val updatedAt: String,
)