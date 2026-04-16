package com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.ui

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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.common.ui.LoadingIndicator
import com.github_explorer.kts_android_kmp.common.ui.RepoCard
import com.github_explorer.kts_android_kmp.common.ui.StatusBarSpacer
import com.github_explorer.kts_android_kmp.common.ui.theme.Dimens
import com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.presentation.UserRepoViewModel
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.private_repos_title
import ktsandroidkmp.composeapp.generated.resources.profile_load_error
import ktsandroidkmp.composeapp.generated.resources.profile_retry
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserReposScreen(
    onBackClick: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenRepo: (owner: String, repo: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    userRepoViewModel: UserRepoViewModel = koinViewModel(),
) {
    val state by userRepoViewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenHorizontalPaddingLarge)
            .padding(top = Dimens.ScreenVerticalPaddingMedium),
        verticalArrangement = Arrangement.Top,
    ) {
        StatusBarSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }

            Text(
                text = stringResource(Res.string.private_repos_title),
                style = MaterialTheme.typography.headlineLarge,
            )

            IconButton(onClick = onOpenSettings) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                )
            }
        }

        Spacer(Modifier.height(Dimens.SpacingLarge))

        when {
            state.isLoading -> LoadingIndicator()

            state.isError -> {
                Text(
                    text = stringResource(Res.string.profile_load_error),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )

                Spacer(Modifier.height(Dimens.SpacingMedium))

                OutlinedButton(onClick = userRepoViewModel::load) {
                    Text(stringResource(Res.string.profile_retry))
                }
            }

            state.repos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "У пользователя пока нет репозиториев",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(items = state.repos, key = { it.id }) { repo ->
                        RepoCard(
                            repo = repo,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onOpenRepo(repo.owner, repo.name) }
                        )
                    }
                }
            }
        }
    }
}
//
//@Composable
//private fun UserRepoCard(repo: GitHubRepo) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//    ) {
//        Column(modifier = Modifier.padding(Dimens.ScreenTotalPaddingSmall)) {
//            Text(
//                text = "${repo.owner} / ${repo.name}",
//                style = MaterialTheme.typography.titleMedium,
//            )
//
//            repo.description?.takeIf { it.isNotBlank() }?.let { description ->
//                Spacer(Modifier.height(6.dp))
//                Text(
//                    text = description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                )
//            }
//
//            Spacer(Modifier.height(8.dp))
//
//            val updatedAt = remember(repo.updatedAt) { formatDate(repo.updatedAt) }
//
//            Text(
//                text = buildString {
//                    repo.language?.takeIf { it.isNotBlank() }?.let { append(it) }
//                    if (repo.language != null) append(" · ")
//                    append("⭐ ${repo.stars}")
//                    append(" · 🍴 ${repo.forks}")
//                    append(" · $updatedAt")
//                },
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//            )
//        }
//    }
//}
//
//private fun formatDate(date: String): String {
//    val instant = Instant.parse(date)
//    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
//
//    return buildString {
//        append(localDate.dayOfMonth.toString().padStart(2, '0'))
//        append('.')
//        append(localDate.monthNumber.toString().padStart(2, '0'))
//        append('.')
//        append(localDate.year)
//    }
//}