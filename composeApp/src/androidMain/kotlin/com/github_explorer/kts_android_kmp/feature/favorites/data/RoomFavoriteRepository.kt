package com.github_explorer.kts_android_kmp.feature.favorites.data

import com.github_explorer.kts_android_kmp.db.dao.FavoriteRepoDao
import com.github_explorer.kts_android_kmp.db.entity.FavoriteRepoEntity
import com.github_explorer.kts_android_kmp.feature.favorites.domain.FavoriteRepository
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomFavoriteRepository(
    private val dao: FavoriteRepoDao,
    private val timeProvider: TimeProvider,
) : FavoriteRepository {

    override fun observeFavorites(): Flow<List<GitHubRepo>> {
        return dao.observeAll().map { list -> list.map { it.toGitHubRepo() } }
    }

    override suspend fun isFavorite(repoId: Long): Boolean = dao.exists(repoId)

    override suspend fun toggleFavorite(repo: GitHubRepo): Boolean {
        val exists = dao.exists(repo.id)
        return if (exists) {
            dao.delete(repo.id)
            false
        } else {
            dao.upsert(repo.toEntity(savedAt = timeProvider.nowMillis()))
            true
        }
    }
}

interface TimeProvider {
    fun nowMillis(): Long
}

internal fun GitHubRepo.toEntity(savedAt: Long): FavoriteRepoEntity {
    return FavoriteRepoEntity(
        repoId = id,
        owner = owner,
        name = name,
        description = description,
        language = language,
        stars = stars,
        forks = forks,
        updatedAt = updatedAt,
        savedAt = savedAt,
    )
}

internal fun FavoriteRepoEntity.toGitHubRepo(): GitHubRepo {
    return GitHubRepo(
        id = repoId,
        owner = owner,
        name = name,
        description = description,
        language = language,
        stars = stars,
        forks = forks,
        updatedAt = updatedAt,
    )
}




