package com.example.kts_android_kmp.feature.mainScreen.domain.cache

import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepoEntity

class NoOpGitHubRepoCache : IGitHubRepoCache {
    override suspend fun saveSearchResultCache(query: String, repos: List<GitHubRepoEntity>) = Unit
    override suspend fun getCachedSearchResult(query: String): List<GitHubRepoEntity> = emptyList()
    override suspend fun clearCacheQuery(query: String) = Unit
}

