package com.github_explorer.kts_android_kmp.feature.repoScreen.data.network

import kotlinx.serialization.Serializable

@Serializable
data class GithubRepoReadmeDto(
    val type: String,
    val name: String,
    val content: String,
    val sha: String,
)
