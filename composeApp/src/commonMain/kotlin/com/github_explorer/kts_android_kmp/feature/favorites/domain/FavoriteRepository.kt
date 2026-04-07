package com.github_explorer.kts_android_kmp.feature.favorites.domain

import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun observeFavorites(): Flow<List<GitHubRepo>>

    suspend fun isFavorite(repoId: Long): Boolean

    suspend fun toggleFavorite(repo: GitHubRepo): Boolean
}

