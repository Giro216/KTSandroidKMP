package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain

import androidx.compose.runtime.Immutable

@Immutable
data class GithubRepoDetails(
    val id: Long,
    val owner: String,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val starsCount: Int,
    val forksCount: Int,
    val openIssuesCount: Int,
    val ownerAvatarUrl: String?,
    val htmlUrl: String,
    val watchersCount: Int,
    val licenseName: String?,
    val defaultBranch: String,
    val updatedAt: String,
)

