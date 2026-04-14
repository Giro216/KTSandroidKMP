package com.github_explorer.kts_android_kmp.feature.repoScreen.domain

data class GithubRepoReadme(
    val decodeContent: String,
    val path: String?,
    val htmlUrl: String?,
    val downloadUrl: String?,
)
