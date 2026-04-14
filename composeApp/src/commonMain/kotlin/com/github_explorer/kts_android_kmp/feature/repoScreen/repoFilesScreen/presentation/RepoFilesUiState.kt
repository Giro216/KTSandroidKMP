package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation

import androidx.compose.runtime.Immutable
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoDirItem
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileContent
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileItemType

@Immutable
data class RepoFilesUiState(
    val owner: String? = null,
    val repo: String? = null,
    val path: String = "",
    val contentType: RepoFileItemType? = null,

    val items: List<RepoDirItem> = emptyList(),
    val fileContent: RepoFileContent? = null,

    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,

    val isEditorVisible: Boolean = false,
    val editorMode: RepoFileEditorMode = RepoFileEditorMode.CREATE,
    val newFileName: String = "",
    val newFileContent: String = "",
    val isUploading: Boolean = false,
)

enum class RepoFileEditorMode {
    CREATE,
    UPDATE,
}

