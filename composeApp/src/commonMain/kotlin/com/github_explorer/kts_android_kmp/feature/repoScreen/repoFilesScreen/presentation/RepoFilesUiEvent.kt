package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation

import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileItemType

sealed interface RepoFilesUiEvent {

    data class Init(
        val owner: String,
        val repo: String,
        val path: String,
        val contentType: RepoFileItemType,
    ) : RepoFilesUiEvent

    data object Refresh : RepoFilesUiEvent

    data object Retry : RepoFilesUiEvent

    data class DirectoryClicked(val path: String) : RepoFilesUiEvent

    data class FileClicked(val path: String) : RepoFilesUiEvent

    data object CreateFileClicked : RepoFilesUiEvent

    data object UpdateFileClicked : RepoFilesUiEvent

    data class NewFileNameChanged(val value: String) : RepoFilesUiEvent

    data class NewFileContentChanged(val value: String) : RepoFilesUiEvent

    data object DismissEditor : RepoFilesUiEvent

    data object ConfirmEditor : RepoFilesUiEvent

}
