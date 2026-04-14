package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation

sealed interface RepoFilesUiEvent {

    data class Init(
        val owner: String,
        val repo: String,
        val path: String,
    ) : RepoFilesUiEvent

    data object Refresh : RepoFilesUiEvent

    data object Retry : RepoFilesUiEvent

    data class DirectoryClicked(val path: String) : RepoFilesUiEvent

    data class FileClicked(val path: String) : RepoFilesUiEvent

    data object CreateFileClicked : RepoFilesUiEvent

    data class NewFileNameChanged(val value: String) : RepoFilesUiEvent

    data class NewFileContentChanged(val value: String) : RepoFilesUiEvent

    data object DismissCreateFileDialog : RepoFilesUiEvent

    data object ConfirmCreateFile : RepoFilesUiEvent

}
