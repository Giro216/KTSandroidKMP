package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDirContentDto(
    val type: String,
    val name: String,
    val path: String,
    val sha: String? = null,
    val size: Long? = null,
    @SerialName("download_url")
    val downloadUrl: String? = null,
    @SerialName("html_url")
    val htmlUrl: String? = null,
    val url: String? = null,
)

