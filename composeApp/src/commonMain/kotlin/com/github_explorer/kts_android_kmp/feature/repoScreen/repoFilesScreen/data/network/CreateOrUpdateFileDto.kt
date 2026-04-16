package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.data.network

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrUpdateFileRequestDto(
    val message: String,
    /** base64 encoded content */
    val content: String,
    val sha: String? = null,
    val branch: String? = null,
)

@Serializable
data class CreateOrUpdateFileResponseDto(
    val content: UpdatedContentDto? = null,
    val commit: CommitDto? = null,
)

@Serializable
data class UpdatedContentDto(
    val sha: String? = null,
    val path: String? = null,
)

@Serializable
data class CommitDto(
    val sha: String? = null,
)

