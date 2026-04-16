package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.data.network

import kotlinx.serialization.Serializable

@Serializable
data class CreateIssueRequestDto(
    val title: String,
    val body: String? = null,
)