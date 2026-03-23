package com.example.kts_android_kmp.feature.mainScreen.cache

import com.example.kts_android_kmp.db.dao.GitHubRepoDao
import com.example.kts_android_kmp.db.entity.GitHubRepoCacheEntity
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.example.kts_android_kmp.feature.mainScreen.domain.cache.IGitHubRepoCache

class RoomGitHubRepoCache(
    private val dao: GitHubRepoDao,
) : IGitHubRepoCache {

    override suspend fun saveSearchResultCache(query: String, repos: List<GitHubRepo>) {
        dao.deleteQuery(query)
        val entities =
            repos.mapIndexed { index, repo -> repo.toCacheEntity(query = query, position = index) }
        dao.upsertAll(entities)
    }

    override suspend fun getCachedSearchResult(query: String): List<GitHubRepo> {
        return dao.getByQuery(query).map { it.toDomain() }
    }

    override suspend fun clearCacheQuery(query: String) {
        dao.deleteQuery(query)
    }
}

private fun GitHubRepo.toCacheEntity(query: String, position: Int): GitHubRepoCacheEntity {
    return GitHubRepoCacheEntity(
        query = query,
        repoId = id,
        owner = owner,
        name = name,
        description = description,
        language = language,
        stars = stars,
        forks = forks,
        updatedAt = updatedAt,
        position = position,
    )
}

private fun GitHubRepoCacheEntity.toDomain(): GitHubRepo {
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

