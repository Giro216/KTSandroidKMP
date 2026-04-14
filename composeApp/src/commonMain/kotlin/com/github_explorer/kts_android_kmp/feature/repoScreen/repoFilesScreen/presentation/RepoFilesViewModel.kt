package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation

import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.FileType
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileItemType
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.CreateRepoFileUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.LoadRepoContentsUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.LoadRepoFileContentUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase.UpdateRepoFileUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.ui.decodeBase64
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.ui.getFileType
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoFilesViewModel(
    private val loadRepoContentsUseCase: LoadRepoContentsUseCase,
    private val createRepoFileUseCase: CreateRepoFileUseCase,
    private val loadRepoFileContentUseCase: LoadRepoFileContentUseCase,
    private val updateRepoFileUseCase: UpdateRepoFileUseCase,
) : BaseViewModel<RepoFilesClickEvent, RepoFilesUiState>(RepoFilesUiState()) {

    fun onEvent(event: RepoFilesUiEvent) {
        when (event) {
            is RepoFilesUiEvent.Init -> {
                updateState {
                    copy(
                        owner = event.owner,
                        repo = event.repo,
                        path = event.path,
                        contentType = event.contentType
                    )
                }
                load()
            }

            RepoFilesUiEvent.Refresh,
            RepoFilesUiEvent.Retry,
                -> load()

            is RepoFilesUiEvent.DirectoryClicked -> {
                acceptLabel(RepoFilesClickEvent.OpenDir(event.path))
            }

            is RepoFilesUiEvent.FileClicked -> {
                acceptLabel(RepoFilesClickEvent.OpenFile(event.path))
            }

            RepoFilesUiEvent.CreateFileClicked -> {
                updateState {
                    copy(
                        isEditorVisible = true,
                        editorMode = RepoFileEditorMode.CREATE,
                        newFileName = "",
                        newFileContent = "",
                    )
                }
            }

            RepoFilesUiEvent.UpdateFileClicked -> {
                openEditorForUpdate()
            }

            RepoFilesUiEvent.DismissEditor -> {
                updateState {
                    copy(
                        isEditorVisible = false,
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

            RepoFilesUiEvent.ConfirmEditor -> {
                when (state.value.editorMode) {
                    RepoFileEditorMode.CREATE -> createFile()
                    RepoFileEditorMode.UPDATE -> updateFile()
                }
            }
        }
    }

    private fun openEditorForUpdate() {
        val file = state.value.fileContent
        if (file == null) {
            acceptLabel(RepoFilesClickEvent.Snackbar("Файл еще не загружен"))
            return
        }

        val type = getFileType(file.name)
        val isEditable = type == FileType.CODE || type == FileType.JSON || type == FileType.TEXT
        if (!isEditable) {
            acceptLabel(RepoFilesClickEvent.Snackbar("Редактирование доступно только для CODE/JSON/TEXT"))
            return
        }

        updateState {
            copy(
                isEditorVisible = true,
                editorMode = RepoFileEditorMode.UPDATE,
                newFileName = file.name,
                newFileContent = decodeBase64(file.content),
            )
        }
    }

    private fun load() {
        val owner = state.value.owner ?: return
        val repo = state.value.repo ?: return
        val path = state.value.path
        val contentType = state.value.contentType ?: return

        when (contentType) {
            RepoFileItemType.FILE -> {
                loadFileContent(owner, repo, path)
            }

            RepoFileItemType.DIR -> {
                loadDirContent(owner, repo, path)
            }
        }
    }

    private fun loadDirContent(owner: String, repo: String, path: String) {
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

    private fun loadFileContent(owner: String, repo: String, path: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, isError = false, errorMessage = null) }

            val result = withContext(Dispatchers.IO) {
                loadRepoFileContentUseCase.getFileContent(owner = owner, repo = repo, path = path)
            }

            result
                .onSuccess { content ->
                    updateState {
                        copy(
                            isLoading = false,
                            fileContent = content,
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
                    Napier.e("Failed to load file content", t)
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
            acceptLabel(RepoFilesClickEvent.Snackbar("Введите имя файла"))
            return
        }
        if (fileName.contains("..")) {
            acceptLabel(RepoFilesClickEvent.Snackbar("Некорректное имя файла"))
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
                            isEditorVisible = false,
                            newFileName = "",
                            newFileContent = "",
                        )
                    }
                    acceptLabel(RepoFilesClickEvent.Snackbar("Файл создан: $fullPath"))
                    load()
                }
                .onFailure { t ->
                    updateState { copy(isUploading = false) }
                    acceptLabel(RepoFilesClickEvent.Snackbar(t.message ?: "Ошибка загрузки"))
                    Napier.e("Failed to create file", t)
                }
        }
    }

    private fun updateFile() {
        val owner = state.value.owner ?: return
        val repo = state.value.repo ?: return
        val path = state.value.path
        val content = state.value.newFileContent

        if (path.isBlank()) {
            acceptLabel(RepoFilesClickEvent.Snackbar("Путь файла не определен"))
            return
        }

        viewModelScope.launch {
            updateState { copy(isUploading = true) }

            val result = withContext(Dispatchers.IO) {
                updateRepoFileUseCase.execute(
                    owner = owner,
                    repo = repo,
                    path = path,
                    contentUtf8 = content,
                    message = "Update ${
                        state.value.newFileName.ifBlank {
                            path.substringAfterLast(
                                '/'
                            )
                        }
                    }",
                )
            }

            result
                .onSuccess {
                    updateState {
                        copy(
                            isUploading = false,
                            isEditorVisible = false,
                            newFileName = "",
                            newFileContent = "",
                        )
                    }
                    acceptLabel(RepoFilesClickEvent.Snackbar("Файл обновлен"))
                    load()
                }
                .onFailure { t ->
                    updateState { copy(isUploading = false) }
                    acceptLabel(RepoFilesClickEvent.Snackbar(t.message ?: "Ошибка обновления"))
                    Napier.e("Failed to update file", t)
                }
        }
    }
}

sealed interface RepoFilesClickEvent {
    data class Snackbar(val message: String) : RepoFilesClickEvent
    data class OpenFile(val path: String) : RepoFilesClickEvent
    data class OpenDir(val path: String) : RepoFilesClickEvent
}