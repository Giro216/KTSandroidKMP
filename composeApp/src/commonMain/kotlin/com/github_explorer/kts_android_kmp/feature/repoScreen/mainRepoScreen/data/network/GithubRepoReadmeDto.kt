package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepoReadmeDto(
    val type: String,
    val name: String,
    val content: String,
    val sha: String,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("download_url")
    val downloadUrl: String,
)
