package com.github_explorer.kts_android_kmp.feature.favorites.data

import com.github_explorer.kts_android_kmp.db.dao.FavoriteRepoDao
import com.github_explorer.kts_android_kmp.db.entity.FavoriteRepoEntity
import com.github_explorer.kts_android_kmp.feature.favorites.domain.FavoriteRepository
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.github_explorer.kts_android_kmp.utils.coRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomFavoriteRepositoryImpl(
    private val dao: FavoriteRepoDao,
) : FavoriteRepository {

    override suspend fun observeFavorites(): Result<Flow<List<GitHubRepo>>> {
        return coRunCatching {
            dao.observeAll().map { list -> list.map { it.toGitHubRepo() } }
        }
    }

    override suspend fun isFavorite(repoId: Long): Result<Boolean> {
        return coRunCatching {
            dao.exists(repoId)
        }
    }

    override suspend fun toggleFavorite(repo: GitHubRepo): Result<Boolean> {
        val exists = dao.exists(repo.id)
        return coRunCatching {
            if (exists) {
                dao.delete(repo.id)
                false
            } else {
                dao.upsert(repo.toEntity(savedAt = nowMillis()))
                true
            }
        }
    }
}

fun nowMillis(): Long = System.currentTimeMillis()

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




