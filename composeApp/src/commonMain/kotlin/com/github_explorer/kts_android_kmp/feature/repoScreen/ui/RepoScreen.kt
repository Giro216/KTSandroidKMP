package com.github_explorer.kts_android_kmp.feature.repoScreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.feature.repoScreen.platform.MarkdownBlock
import com.github_explorer.kts_android_kmp.feature.repoScreen.presentation.RepoUiEvent
import com.github_explorer.kts_android_kmp.feature.repoScreen.presentation.RepoUiState
import com.github_explorer.kts_android_kmp.feature.repoScreen.presentation.RepoViewModel
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.fork_logo
import ktsandroidkmp.composeapp.generated.resources.repo_title
import ktsandroidkmp.composeapp.generated.resources.star_logo
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RepoScreen(
    viewModel: RepoViewModel = koinViewModel(),
    owner: String,
    repo: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(owner, repo) {
        viewModel.onEvent(RepoUiEvent.Init(owner = owner, repo = repo))
    }

    RepoScreenContent(
        state = state,
        onBackClick = onBackClick,
        onToggleFavorite = { viewModel.onEvent(RepoUiEvent.ToggleFavorite) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RepoScreenContent(
    state: RepoUiState,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.details?.fullName ?: stringResource(Res.string.repo_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        val icon = if (state.isStarredLocally) {
                            Icons.Filled.Star
                        } else {
                            Icons.Outlined.StarBorder
                        }
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        if (state.isLoading && state.details == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LoadingIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
            ) {
                state.details?.let { details ->
                    Text(
                        text = details.name,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (!details.description.isNullOrBlank()) {
                        Text(
                            text = details.description,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = stringResource(Res.string.star_logo) + "${details.starsCount}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = stringResource(Res.string.fork_logo) + " ${details.forksCount}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "🐞 ${details.openIssuesCount}",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    details.language?.let { lang ->
                        Text(
                            text = "Language: $lang",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "README",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                when {
                    state.isReadmeLoading -> {
                        CircularProgressIndicator()
                    }

                    state.readme != null -> {
//                        Text(
//                            text = state.readme.decodeContent,
//                            style = MaterialTheme.typography.bodySmall,
//                        )
//                        onMarkdownContent(state.readme.decodeContent)
                        MarkdownBlock(
                            markdown = state.readme.decodeContent,
                        )
                    }

                    state.isError -> {
                        Text(
                            text = state.errorMessage ?: "Failed to load README",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}
