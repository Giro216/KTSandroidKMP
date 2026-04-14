package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain

import androidx.compose.runtime.Immutable

@Immutable
data class RepoFileContent(
    val name: String,
    val path: String,
    val type: RepoFileItemType,
    val sha: String,
    val size: Long,
    val content: String,
    val encoding: String,
)

/*
      "required": [
        "_links",
        "git_url",
        "html_url",
        "download_url",
        "name",
        "path",
        "sha",
        "size",
        "type",
        "url",
        "content",
        "encoding"
      ]
 */