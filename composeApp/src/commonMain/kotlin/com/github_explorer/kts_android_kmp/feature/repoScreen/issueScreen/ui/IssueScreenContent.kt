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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github_explorer.kts_android_kmp.common.ui.LoadingIndicator
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssue
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation.GithubIssueUiState
import io.github.aakira.napier.Napier
import ktsandroidkmp.composeapp.generated.resources.OK
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.cancel_ru
import ktsandroidkmp.composeapp.generated.resources.issues_screen_create_issue_en
import ktsandroidkmp.composeapp.generated.resources.issues_screen_create_ru
import ktsandroidkmp.composeapp.generated.resources.issues_screen_creating_ru
import ktsandroidkmp.composeapp.generated.resources.issues_screen_issue_description_ru
import ktsandroidkmp.composeapp.generated.resources.issues_screen_issue_status_closed
import ktsandroidkmp.composeapp.generated.resources.issues_screen_issue_status_open
import ktsandroidkmp.composeapp.generated.resources.issues_screen_issue_status_unknown
import ktsandroidkmp.composeapp.generated.resources.issues_screen_issue_title_ru
import ktsandroidkmp.composeapp.generated.resources.issues_screen_load_issue_error_ru
import ktsandroidkmp.composeapp.generated.resources.issues_screen_no_open_issue_ru
import ktsandroidkmp.composeapp.generated.resources.issues_screen_refresh_ru
import ktsandroidkmp.composeapp.generated.resources.issues_screen_retry_ru
import ktsandroidkmp.composeapp.generated.resources.issues_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueScreenContent(
    state: GithubIssueUiState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onOpenCreateIssue: () -> Unit,
    onDismissCreateIssue: () -> Unit,
    onDismissOwnershipWarning: () -> Unit,
    onDismissCreateIssueSuccess: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit,
    onCreateIssue: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.issues_title),
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
                    TopBarActions(
                        state = state,
                        onRefresh = onRefresh,
                        onOpenCreateIssue = onOpenCreateIssue
                    )
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp),
        ) {
            state.createIssueSuccessMessage?.let { success ->
                Banner(
                    text = success,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClose = onDismissCreateIssueSuccess,
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            IssueStateHandler(state = state, onRefresh = onRefresh)
        }

        if (state.isCreateDialogOpen) {
            CreateDialog(
                state = state,
                onTitleChanged = onTitleChanged,
                onBodyChanged = onBodyChanged,
                onCreateIssue = onCreateIssue,
                onDismissCreateIssue = onDismissCreateIssue,
                onDismissOwnershipWarning = onDismissOwnershipWarning,
            )
        }
    }
}

@Composable
private fun TopBarActions(
    state: GithubIssueUiState,
    onRefresh: () -> Unit,
    onOpenCreateIssue: () -> Unit
) {
    Text(
        text = if (state.isLoading) "" else stringResource(Res.string.issues_screen_refresh_ru),
        modifier = Modifier
            .padding(end = 16.dp)
            .padding(vertical = 8.dp)
            .clickable(enabled = !state.isLoading, onClick = onRefresh),
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )

    IconButton(
        onClick = onOpenCreateIssue,
        modifier = Modifier
            .size(40.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
            ),
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(Res.string.issues_screen_create_issue_en),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }

    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
private fun IssueStateHandler(state: GithubIssueUiState, onRefresh: () -> Unit) {
    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LoadingIndicator()
            }
        }

        state.isError -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Napier.e("Failed to load issues: ${state.errorMessage ?: "Unknown error"}")
                Text(
                    text = stringResource(Res.string.issues_screen_load_issue_error_ru),
                    color = MaterialTheme.colorScheme.error,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(Res.string.issues_screen_retry_ru),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onRefresh)
                )
            }
        }

        state.issueList.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(Res.string.issues_screen_no_open_issue_ru))
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
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

@Composable
private fun CreateDialog(
    state: GithubIssueUiState,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit,
    onCreateIssue: () -> Unit,
    onDismissCreateIssue: () -> Unit,
    onDismissOwnershipWarning: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissCreateIssue,
        title = { Text(stringResource(Res.string.issues_screen_no_open_issue_ru)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.ownershipWarningMessage?.let { warning ->
                    Banner(
                        text = warning,
                        backgroundColor = MaterialTheme.colorScheme.errorContainer,
                        textColor = MaterialTheme.colorScheme.onErrorContainer,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        onClose = onDismissOwnershipWarning,
                    )
                }
                OutlinedTextField(
                    value = state.issueTitleInput,
                    onValueChange = onTitleChanged,
                    label = { Text(stringResource(Res.string.issues_screen_issue_title_ru)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = state.issueBodyInput,
                    onValueChange = onBodyChanged,
                    label = { Text(stringResource(Res.string.issues_screen_issue_description_ru)) },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                )

                state.createIssueError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onCreateIssue,
                enabled = !state.isCreatingIssue,
            ) {
                Text(
                    if (state.isCreatingIssue) stringResource(Res.string.issues_screen_creating_ru) else stringResource(
                        Res.string.issues_screen_create_ru
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissCreateIssue,
                enabled = !state.isCreatingIssue,
            ) {
                Text(stringResource(Res.string.cancel_ru))
            }
        },
    )
}

@Composable
private fun Banner(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = onClose) {
            Text(stringResource(Res.string.OK), color = textColor)
        }
    }
}

@Composable
private fun IssueRow(issue: GithubIssue) {
    val horizontalPadding = 6.dp
    val verticalPadding = 3.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Spacer(modifier = Modifier.height(verticalPadding))
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
                    GithubIssue.State.OPEN -> stringResource(Res.string.issues_screen_issue_status_open)
                    GithubIssue.State.CLOSED -> stringResource(Res.string.issues_screen_issue_status_closed)
                    GithubIssue.State.UNKNOWN -> stringResource(Res.string.issues_screen_issue_status_unknown)
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Text(
            text = issue.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(
                horizontal = horizontalPadding,
            )
        )

        Spacer(modifier = Modifier.height(verticalPadding))
    }
}

@Preview
@Composable
private fun IssueScreenContentPreview() {
    MaterialTheme {
        IssueScreenContent(
            state = GithubIssueUiState(
                isLoading = false,
                issueList = listOf(
                    GithubIssue(
                        id = 1L,
                        number = 42,
                        title = "Preview issue title",
                        state = GithubIssue.State.OPEN,
                    ),
                    GithubIssue(
                        id = 2L,
                        number = 43,
                        title = "Second preview issue with a longer title",
                        state = GithubIssue.State.CLOSED,
                    ),
                ),
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

