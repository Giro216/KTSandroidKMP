package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.common.ui.theme.AppColors
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoDetails
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoReadme
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform.MarkdownBlock
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.presentation.RepoUiEvent
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.presentation.RepoUiState
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.presentation.RepoViewModel
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.fork_logo
import ktsandroidkmp.composeapp.generated.resources.repo_screen_Language
import ktsandroidkmp.composeapp.generated.resources.repo_screen_readme_load_error
import ktsandroidkmp.composeapp.generated.resources.repo_screen_readme_title
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
    onOpenIssues: (owner: String, repo: String) -> Unit,
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
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RepoScreenContent(
    state: RepoUiState,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onOpenIssues: () -> Unit,
    renderReadme: @Composable (String) -> Unit = { markdown ->
        MarkdownBlock(markdown = markdown)
    },
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.details?.let { "${it.owner} / ${it.name}" }
                            ?: stringResource(Res.string.repo_title),
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
                    IconButton(onClick = onToggleFavorite) {
                        val (icon, tint) = if (state.isStarredLocally) {
                            Icons.Filled.Star to MaterialTheme.colorScheme.primary
                        } else {
                            Icons.Outlined.StarBorder to MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        Icon(imageVector = icon, tint = tint, contentDescription = null)
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
                            text = stringResource(Res.string.repo_screen_Language) + lang,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onOpenIssues,
                    enabled = (state.details?.openIssuesCount ?: 0) > 0,
                    modifier = modifier
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Text(
                            text = "Issues",
                            style = MaterialTheme.typography.bodyLargeEmphasized,
                            color = AppColors.PrimaryBlue,
                        )

                        Text(
                            text = "${state.details?.openIssuesCount ?: 0}",
                            style = MaterialTheme.typography.bodyLargeEmphasized,
                            color = AppColors.PrimaryBlue,
                        )
                    }

                }

                Text(
                    text = stringResource(Res.string.repo_screen_readme_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))

                when {
                    state.isReadmeLoading -> {
                        CircularProgressIndicator()
                    }

                    state.readme != null -> {
                        val resolveReadme = resolveRelativeImages(
                            markdown = state.readme.decodeContent,
                            baseUrl = state.readme.downloadUrl ?: ""
                        )

                        renderReadme(resolveReadme)
                    }

                    state.isError -> {
                        Text(
                            text = state.errorMessage
                                ?: stringResource(Res.string.repo_screen_readme_load_error),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

fun resolveRelativeImages(
    markdown: String,
    baseUrl: String
): String {
    val normalizedBaseUrl = baseUrl
        .replace(Regex("""/README\.md$""", RegexOption.IGNORE_CASE), "")
        .trimEnd('/')

    return markdown.replace(
        Regex("""!\[(.*?)\]\((?!http)(.*?)\)""")
    ) {
        val alt = it.groupValues[1]
        val path = it.groupValues[2]
        val fullUrl = normalizedBaseUrl + "/" + path.trimStart('/')

        "![$alt]($fullUrl)"
    }
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
            renderReadme = { markdown ->
                Text(
                    text = markdown,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
        )
    }
}


