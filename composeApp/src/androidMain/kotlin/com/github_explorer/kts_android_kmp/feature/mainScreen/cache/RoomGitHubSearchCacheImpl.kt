package com.github_explorer.kts_android_kmp.feature.mainScreen.cache

import com.github_explorer.kts_android_kmp.db.dao.GitHubSearchCacheDao
import com.github_explorer.kts_android_kmp.db.entity.GitHubRepoEntity
import com.github_explorer.kts_android_kmp.db.entity.GitHubSearchQueryEntity
import com.github_explorer.kts_android_kmp.db.entity.GitHubSearchResultEntity
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.cache.GitHubRepoCache

class RoomGitHubSearchCacheImpl(
    private val dao: GitHubSearchCacheDao,
) : GitHubRepoCache {

    override suspend fun saveSearchResultCache(query: String, repos: List<GitHubRepo>) {
        val normalizedQuery = query.normalizeForCacheKey()

        val queryId = dao.getQueryId(normalizedQuery)
            ?: run {
                val insertedId =
                    dao.insertQuery(GitHubSearchQueryEntity(queryNormalized = normalizedQuery))
                if (insertedId != -1L) insertedId
                else error("Failed to create search query row for '$normalizedQuery'")
            }

        val repoEntities = repos.map { it.toRepoEntity() }
        val resultEntities = repos.mapIndexed { index, repo ->
            GitHubSearchResultEntity(
                queryId = queryId,
                repoId = repo.id,
                position = index,
            )
        }

        dao.replaceQueryResults(
            queryId = queryId,
            repos = repoEntities,
            results = resultEntities,
        )
    }

    override suspend fun getCachedSearchResult(query: String): List<GitHubRepo> {
        val normalizedQuery = query.normalizeForCacheKey()
        val queryId = dao.getQueryId(normalizedQuery) ?: return emptyList()
        return dao.getReposByQueryId(queryId).map { it.toDomain() }
    }

    override suspend fun clearCacheQuery(query: String) {
        val normalizedQuery = query.normalizeForCacheKey()
        dao.deleteQuery(normalizedQuery)
    }
}

private fun String.normalizeForCacheKey(): String = trim().lowercase()

private fun GitHubRepo.toRepoEntity(): GitHubRepoEntity {
    return GitHubRepoEntity(
        repoId = id,
        owner = owner,
        name = name,
        description = description,
        language = language,
        stars = stars,
        forks = forks,
        updatedAt = updatedAt,
    )
}

private fun GitHubRepoEntity.toDomain(): GitHubRepo {
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

