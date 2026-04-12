package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.common.ui.LoadingIndicator
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
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IssueScreenContent(
    state: GithubIssueUiState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Issues",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    Text(
                        text = if (state.isLoading) "" else "Обновить",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .padding(vertical = 8.dp)
                            .clickable(enabled = !state.isLoading, onClick = onRefresh),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    LoadingIndicator()
                }
            }

            state.isError -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = state.errorMessage ?: "Ошибка загрузки issues",
                        color = MaterialTheme.colorScheme.error,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Повторить",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable(onClick = onRefresh)
                            .padding(8.dp),
                    )
                }
            }

            state.issueList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Открытых задач нет")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        items = state.issueList,
                        key = { it.id },
                    ) { issue ->
                        IssueRow(issue = issue)
                    }
                }
            }
        }
    }
}

@Composable
private fun IssueRow(issue: GithubIssue) {
    val horizontalPadding = 6.dp
    val verticalPadding = 2.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "#${issue.number}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = when (issue.state) {
                    GithubIssue.State.OPEN -> "OPEN"
                    GithubIssue.State.CLOSED -> "CLOSED"
                    GithubIssue.State.UNKNOWN -> "UNKNOWN"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(2.dp))

        Text(
            text = issue.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding)
        )
    }
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
        )
    }
}
