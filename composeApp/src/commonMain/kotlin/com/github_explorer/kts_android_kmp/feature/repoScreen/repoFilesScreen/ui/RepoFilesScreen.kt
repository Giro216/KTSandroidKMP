package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileItemType
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation.RepoFilesClickEvent
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation.RepoFilesUiEvent
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation.RepoFilesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoFilesScreen(
    owner: String,
    repo: String,
    path: String,
    contentType: RepoFileItemType,
    onBackClick: () -> Unit,
    onOpenContent: (String, RepoFileItemType) -> Unit,
    viewModel: RepoFilesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(owner, repo, path) {
        viewModel.onEvent(
            RepoFilesUiEvent.Init(
                owner = owner,
                repo = repo,
                path = path,
                contentType = contentType
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RepoFilesClickEvent.Snackbar -> snackbarHostState.showSnackbar(event.message)
                is RepoFilesClickEvent.OpenFile -> onOpenContent(event.path, RepoFileItemType.FILE)
                is RepoFilesClickEvent.OpenDir -> onOpenContent(event.path, RepoFileItemType.DIR)
            }
        }
    }

    RepoFilesScreenContent(
        state = state,
        contentType = contentType,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent,
    )
}