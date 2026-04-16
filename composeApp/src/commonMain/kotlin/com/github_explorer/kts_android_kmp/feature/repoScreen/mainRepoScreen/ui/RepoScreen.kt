package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoDetails
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoReadme
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.presentation.RepoUiEvent
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.presentation.RepoUiState
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.presentation.RepoViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RepoScreen(
    viewModel: RepoViewModel = koinViewModel(),
    owner: String,
    repo: String,
    onBackClick: () -> Unit,
    onOpenIssues: (owner: String, repo: String) -> Unit,
    onOpenCode: (owner: String, repo: String) -> Unit,
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
        onOpenIssues = { onOpenIssues(owner, repo) },
        onOpenCode = { onOpenCode(owner, repo) },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun RepoScreenContentPreview() {
    MaterialTheme {
        RepoScreenContent(
            state = RepoUiState(
                isLoading = false,
                isError = false,
                details = GithubRepoDetails(
                    id = 1L,
                    owner = "octocat",
                    name = "Hello-World",
                    fullName = "octocat/Hello-World",
                    description = "This your first repo!",
                    language = "Kotlin",
                    starsCount = 1532,
                    forksCount = 321,
                    openIssuesCount = 17,
                    ownerAvatarUrl = null,
                    htmlUrl = "https://github.com/octocat/Hello-World",
                    watchersCount = 800,
                    licenseName = "MIT",
                    defaultBranch = "main",
                    updatedAt = "2026-04-13T10:00:00Z",
                ),
                readme = GithubRepoReadme(
                    decodeContent = "# Hello-World\n\nSample README for preview.",
                    path = "README.md",
                    htmlUrl = "https://github.com/octocat/Hello-World/blob/main/README.md",
                    downloadUrl = "https://raw.githubusercontent.com/octocat/Hello-World/main/README.md",
                ),
                isStarredLocally = true,
            ),
            onBackClick = {},
            onToggleFavorite = {},
            onOpenIssues = {},
            onOpenCode = {},
            renderReadme = { markdown ->
                Text(
                    text = markdown,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
        )
    }
}


