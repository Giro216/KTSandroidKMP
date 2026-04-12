package com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase

import com.github_explorer.kts_android_kmp.feature.favorites.domain.FavoriteRepository
import com.github_explorer.kts_android_kmp.feature.favorites.domain.toGitHubRepo
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoDetails

class ToggleFavoriteUseCase(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(repo: GitHubRepo): Boolean = repository.toggleFavorite(repo)

    suspend operator fun invoke(repo: GithubRepoDetails): Boolean =
        repository.toggleFavorite(repo.toGitHubRepo())
}

