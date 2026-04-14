package com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase

import com.github_explorer.kts_android_kmp.feature.favorites.domain.FavoriteRepository
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase(
    private val repository: FavoriteRepository,
) {
    operator fun invoke(): Flow<List<GitHubRepo>> = repository.observeFavorites()
}

