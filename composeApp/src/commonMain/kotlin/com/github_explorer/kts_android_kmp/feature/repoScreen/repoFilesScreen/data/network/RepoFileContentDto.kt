package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoFileContentDto(
    val name: String,
    val path: String,
    val type: String,
    val sha: String,
    val size: Long,
    val content: String,
    val encoding: String,
    @SerialName("download_url")
    val downloadUrl: String? = null,
    @SerialName("html_url")
    val htmlUrl: String? = null,
    val url: String? = null,
) {
}