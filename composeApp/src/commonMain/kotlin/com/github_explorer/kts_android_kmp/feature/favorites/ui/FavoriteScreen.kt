package com.github_explorer.kts_android_kmp.feature.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github_explorer.kts_android_kmp.common.ui.RepoCard
import com.github_explorer.kts_android_kmp.common.ui.theme.Dimens.ScreenHorizontalPaddingSmall
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.favorite_no_repos
import ktsandroidkmp.composeapp.generated.resources.favorite_title
import org.jetbrains.compose.resources.stringResource


@Composable
fun FavoriteScreen(
    repos: List<GitHubRepo>,
    favoriteRepoIds: Set<Long>,
    onOpenRepo: (owner: String, repo: String) -> Unit,
    onToggleFavorite: (GitHubRepo) -> Unit,
    lazyColumnModifier: Modifier,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item(key = "favorites_title") {
            Text(
                text = stringResource(Res.string.favorite_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = lazyColumnModifier
                    .fillMaxWidth()
                    .padding(horizontal = ScreenHorizontalPaddingSmall)
            )
        }

        if (repos.isEmpty()) {
            item(key = "favorites_empty") {
                Text(
                    text = stringResource(Res.string.favorite_no_repos),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = ScreenHorizontalPaddingSmall),
                )
            }
        }

        items(items = repos, key = { it.id }) { repo ->
            RepoCard(
                repo = repo,
                modifier = Modifier.padding(horizontal = ScreenHorizontalPaddingSmall),
                onClick = { onOpenRepo(repo.owner, repo.name) },
                isFavorite = favoriteRepoIds.contains(repo.id),
                onFavoriteClick = { onToggleFavorite(repo) },
            )
        }
    }
}