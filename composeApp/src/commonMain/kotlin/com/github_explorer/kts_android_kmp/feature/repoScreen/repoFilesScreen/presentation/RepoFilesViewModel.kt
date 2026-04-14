package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation

import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.CreateRepoFileUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.LoadRepoContentsUseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoFilesViewModel(
    private val loadRepoContentsUseCase: LoadRepoContentsUseCase,
    private val createRepoFileUseCase: CreateRepoFileUseCase,
) : BaseViewModel<RepoFilesOneShotEvent, RepoFilesUiState>(RepoFilesUiState()) {

    fun onEvent(event: RepoFilesUiEvent) {
        when (event) {
            is RepoFilesUiEvent.Init -> {
                updateState {
                    copy(owner = event.owner, repo = event.repo, path = event.path)
                }
                load()
            }

            RepoFilesUiEvent.Refresh,
            RepoFilesUiEvent.Retry,
                -> load()

            is RepoFilesUiEvent.DirectoryClicked -> {
                acceptLabel(RepoFilesOneShotEvent.OpenPath(event.path))
            }

            is RepoFilesUiEvent.FileClicked -> {
                acceptLabel(RepoFilesOneShotEvent.Snackbar("Просмотр/редактирование файла пока не реализованы: ${event.path}"))
            }

            RepoFilesUiEvent.CreateFileClicked -> {
                updateState { copy(showCreateFileDialog = true) }
            }

            RepoFilesUiEvent.DismissCreateFileDialog -> {
                updateState {
                    copy(
                        showCreateFileDialog = false,
                        newFileName = "",
                        newFileContent = "",
                    )
                }
            }

            is RepoFilesUiEvent.NewFileNameChanged -> {
                updateState { copy(newFileName = event.value) }
            }

            is RepoFilesUiEvent.NewFileContentChanged -> {
                updateState { copy(newFileContent = event.value) }
            }

            RepoFilesUiEvent.ConfirmCreateFile -> {
                createFile()
            }
        }
    }

    private fun load() {
        val owner = state.value.owner ?: return
        val repo = state.value.repo ?: return
        val path = state.value.path

        viewModelScope.launch {
            updateState { copy(isLoading = true, isError = false, errorMessage = null) }

            val result = withContext(Dispatchers.IO) {
                loadRepoContentsUseCase.execute(owner = owner, repo = repo, path = path)
            }

            result
                .onSuccess { items ->
                    updateState {
                        copy(
                            isLoading = false,
                            items = items,
                            isError = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { t ->
                    updateState {
                        copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = t.message
                        )
                    }
                    Napier.e("Failed to load repo contents", t)
                }
        }
    }

    private fun createFile() {
        val owner = state.value.owner ?: return
        val repo = state.value.repo ?: return
        val dirPath = state.value.path.trim('/').takeIf { it.isNotBlank() }
        val fileName = state.value.newFileName.trim().trimStart('/')
        val content = state.value.newFileContent

        if (fileName.isBlank()) {
            acceptLabel(RepoFilesOneShotEvent.Snackbar("Введите имя файла"))
            return
        }
        if (fileName.contains("..")) {
            acceptLabel(RepoFilesOneShotEvent.Snackbar("Некорректное имя файла"))
            return
        }

        val fullPath = listOfNotNull(dirPath, fileName).joinToString("/")

        viewModelScope.launch {
            updateState { copy(isUploading = true) }

            val result = withContext(Dispatchers.IO) {
                createRepoFileUseCase.execute(
                    owner = owner,
                    repo = repo,
                    path = fullPath,
                    contentUtf8 = content,
                    message = "Add $fileName",
                )
            }

            result
                .onSuccess {
                    updateState {
                        copy(
                            isUploading = false,
                            showCreateFileDialog = false,
                            newFileName = "",
                            newFileContent = "",
                        )
                    }
                    acceptLabel(RepoFilesOneShotEvent.Snackbar("Файл создан: $fullPath"))
                    load()
                }
                .onFailure { t ->
                    updateState { copy(isUploading = false) }
                    acceptLabel(RepoFilesOneShotEvent.Snackbar(t.message ?: "Ошибка загрузки"))
                    Napier.e("Failed to create file", t)
                }
        }
    }
}

sealed interface RepoFilesOneShotEvent {
    data class Snackbar(val message: String) : RepoFilesOneShotEvent
    data class OpenPath(val path: String) : RepoFilesOneShotEvent
}