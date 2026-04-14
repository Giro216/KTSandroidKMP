package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation

import androidx.compose.runtime.Immutable
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoDirItem

@Immutable
data class RepoFilesUiState(
    val owner: String? = null,
    val repo: String? = null,
    val path: String = "",

    val isLoading: Boolean = false,
    val items: List<RepoDirItem> = emptyList(),

    val isError: Boolean = false,
    val errorMessage: String? = null,

    val showCreateFileDialog: Boolean = false,
    val newFileName: String = "",
    val newFileContent: String = "",
    val isUploading: Boolean = false,
)
