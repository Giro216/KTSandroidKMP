package com.github_explorer.kts_android_kmp.feature.repoScreen.domain

import kotlin.time.Instant

data class GithubRepoDetails(
    val id: Long,
    val name: String,
    val fullName: String,
    val ownerName: String,
    val ownerAvatarUrl: String?,
    val description: String?,
    val htmlUrl: String,
    val starsCount: Int,
    val forksCount: Int,
    val openIssuesCount: Int,
    val watchersCount: Int,
    val language: String?,
    val licenseName: String?,
    val topics: List<String>,
    val homepage: String?,
    val defaultBranch: String,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val pushedAt: Instant?,
)
