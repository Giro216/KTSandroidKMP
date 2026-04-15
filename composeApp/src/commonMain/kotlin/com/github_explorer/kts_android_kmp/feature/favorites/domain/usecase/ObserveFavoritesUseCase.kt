package com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase

import com.github_explorer.kts_android_kmp.feature.favorites.domain.FavoriteRepository
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(): Result<Flow<List<GitHubRepo>>> {
        return repository.observeFavorites()
    }
}

