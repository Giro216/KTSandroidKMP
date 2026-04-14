package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssue
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation.GithubIssueUiEvent
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation.GithubIssueUiState
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation.IssueViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IssueScreen(
    owner: String,
    repo: String,
    onBackClick: () -> Unit,
    viewModel: IssueViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(owner, repo) {
        viewModel.onEvent(GithubIssueUiEvent.Init(owner = owner, repo = repo))
    }

    IssueScreenContent(
        state = state,
        onBackClick = onBackClick,
        onRefresh = { viewModel.onEvent(GithubIssueUiEvent.RefreshLoadIssues) },
        onOpenCreateIssue = { viewModel.onEvent(GithubIssueUiEvent.OpenCreateIssueDialog) },
        onDismissCreateIssue = { viewModel.onEvent(GithubIssueUiEvent.DismissCreateIssueDialog) },

        onDismissOwnershipWarning = { viewModel.onEvent(GithubIssueUiEvent.DismissOwnershipWarning) },
        onDismissCreateIssueSuccess = { viewModel.onEvent(GithubIssueUiEvent.DismissCreateIssueSuccess) },
        onTitleChanged = { viewModel.onEvent(GithubIssueUiEvent.IssueTitleChanged(it)) },
        onBodyChanged = { viewModel.onEvent(GithubIssueUiEvent.IssueBodyChanged(it)) },
        onCreateIssue = { viewModel.onEvent(GithubIssueUiEvent.CreateIssue) },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun RepoScreenContentPreview() {
    MaterialTheme {
        IssueScreenContent(
            state = GithubIssueUiState(
                issueList = arrayListOf(
                    GithubIssue(
                        id = 1L,
                        number = 123,
                        title = "Issue title example",
                        state = GithubIssue.State.OPEN,
                    ),
                    GithubIssue(
                        id = 2L,
                        number = 124,
                        title = "Another issue title example that is a bit longer to test text overflow",
                        state = GithubIssue.State.CLOSED,
                    ),
                ),
                isLoading = false,
                isError = false,
                errorMessage = null,
            ),
            onBackClick = {},
            onRefresh = {},
            onOpenCreateIssue = {},
            onDismissCreateIssue = {},
            onDismissOwnershipWarning = {},
            onDismissCreateIssueSuccess = {},
            onTitleChanged = {},
            onBodyChanged = {},
            onCreateIssue = {},
        )
    }
}
