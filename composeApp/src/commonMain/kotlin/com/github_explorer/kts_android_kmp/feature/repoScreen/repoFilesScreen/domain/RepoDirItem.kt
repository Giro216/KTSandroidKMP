package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain

import androidx.compose.runtime.Immutable

@Immutable
data class RepoDirItem(
    val name: String,
    val path: String,
    val type: RepoFileItemType,
    val sha: String? = null,
    val size: Long? = null,
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
          "url"
        ]
 */
