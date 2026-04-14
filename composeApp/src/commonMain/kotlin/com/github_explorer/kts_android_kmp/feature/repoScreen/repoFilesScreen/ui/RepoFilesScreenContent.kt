package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoDirItem
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileItemType
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation.RepoFilesUiEvent
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation.RepoFilesUiState
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.main_screen_retry_button
import ktsandroidkmp.composeapp.generated.resources.repo_files_screen_edit
import ktsandroidkmp.composeapp.generated.resources.repo_files_screen_file_icon
import ktsandroidkmp.composeapp.generated.resources.repo_files_screen_folder_icon
import ktsandroidkmp.composeapp.generated.resources.repo_files_screen_loading_error
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoFilesScreenContent(
    state: RepoFilesUiState,
    contentType: RepoFileItemType,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onEvent: (RepoFilesUiEvent) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    if (state.isEditorVisible) {
        FileEditorScreen(
            mode = state.editorMode,
            fileName = state.newFileName,
            content = state.newFileContent,
            isUploading = state.isUploading,
            onFileNameChanged = { onEvent(RepoFilesUiEvent.NewFileNameChanged(it)) },
            onContentChanged = { onEvent(RepoFilesUiEvent.NewFileContentChanged(it)) },
            onClose = { onEvent(RepoFilesUiEvent.DismissEditor) },
            onSave = { onEvent(RepoFilesUiEvent.ConfirmEditor) },
            modifier = modifier,
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.path.takeIf { it.isNotBlank() } ?: "/",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    when (contentType) {
                        RepoFileItemType.DIR -> {
                            IconButton(onClick = { onEvent(RepoFilesUiEvent.CreateFileClicked) }) {
                                Icon(Icons.Filled.Add, contentDescription = null)
                            }
                        }

                        RepoFileItemType.FILE -> {
                            IconButton(onClick = { onEvent(RepoFilesUiEvent.UpdateFileClicked) }) {
                                Text(text = stringResource(Res.string.repo_files_screen_edit))
                            }
                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            if (state.isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
                Spacer(Modifier.height(12.dp))
            }

            if (state.isError) {
                Text(
                    text = state.errorMessage
                        ?: stringResource(Res.string.repo_files_screen_loading_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { onEvent(RepoFilesUiEvent.Retry) }) {
                    Text(stringResource(Res.string.main_screen_retry_button))
                }
                Spacer(Modifier.height(12.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                when (contentType) {
                    RepoFileItemType.DIR -> {
                        items(state.items, key = { it.path }) { item ->
                            RepoFileRow(
                                item = item,
                                onClick = {
                                    when (item.type) {
                                        RepoFileItemType.DIR -> onEvent(
                                            RepoFilesUiEvent.DirectoryClicked(
                                                item.path
                                            )
                                        )

                                        RepoFileItemType.FILE -> onEvent(
                                            RepoFilesUiEvent.FileClicked(
                                                item.path
                                            )
                                        )
                                    }
                                },
                            )
                        }
                    }

                    RepoFileItemType.FILE -> {
                        item {
                            state.fileContent?.let { file ->
                                FileViewer(file = file)
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun RepoFileRow(
    item: RepoDirItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val prefix = when (item.type) {
        RepoFileItemType.DIR -> stringResource(Res.string.repo_files_screen_folder_icon)
        RepoFileItemType.FILE -> stringResource(Res.string.repo_files_screen_file_icon)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = prefix)
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = item.path,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}