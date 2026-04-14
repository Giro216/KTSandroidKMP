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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github_explorer.kts_android_kmp.common.ui.theme.AppColors
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoDetails
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform.MarkdownBlock
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.presentation.RepoUiState
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.fork_logo
import ktsandroidkmp.composeapp.generated.resources.issue_logo
import ktsandroidkmp.composeapp.generated.resources.issues_title
import ktsandroidkmp.composeapp.generated.resources.repo_screen_Language
import ktsandroidkmp.composeapp.generated.resources.repo_screen_readme_load_error
import ktsandroidkmp.composeapp.generated.resources.repo_screen_readme_title
import ktsandroidkmp.composeapp.generated.resources.repo_title
import ktsandroidkmp.composeapp.generated.resources.star_logo
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RepoScreenContent(
    state: RepoUiState,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onOpenIssues: () -> Unit,
    onOpenCode: () -> Unit,
    renderReadme: @Composable (String) -> Unit = { markdown ->
        MarkdownBlock(markdown = markdown)
    },
    modifier: Modifier = Modifier.Companion,
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
                PrintRepoDetails(state.details)

                Spacer(modifier = Modifier.height(16.dp))

                IssueButton(state = state, onOpenIssues = onOpenIssues, modifier = modifier)

                CodeButton(state = state, onOpenCode = onOpenCode)

                PrintReadmeSection(state = state, renderReadme = renderReadme)
            }
        }
    }
}

@Composable
fun PrintRepoDetails(details: GithubRepoDetails?) {
    details?.let { details ->
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
                text = stringResource(Res.string.star_logo) + details.starsCount,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(Res.string.fork_logo) + details.forksCount,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(Res.string.issue_logo) + details.openIssuesCount,
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
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IssueButton(state: RepoUiState, onOpenIssues: () -> Unit, modifier: Modifier) {
    OutlinedButton(
        onClick = onOpenIssues,
        enabled = (state.details?.openIssuesCount ?: 0) > 0,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(Res.string.issues_title),
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
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CodeButton(state: RepoUiState, onOpenCode: () -> Unit) {
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedButton(
        onClick = onOpenCode,
        enabled = state.details != null,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Code",
            style = MaterialTheme.typography.bodyLargeEmphasized,
            color = AppColors.PrimaryBlue,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
    }
}

@Composable
fun PrintReadmeSection(state: RepoUiState, renderReadme: @Composable (String) -> Unit) {
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

private fun resolveRelativeImages(
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