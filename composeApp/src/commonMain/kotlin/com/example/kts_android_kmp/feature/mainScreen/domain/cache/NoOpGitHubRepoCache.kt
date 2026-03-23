package com.example.kts_android_kmp.feature.mainScreen.domain.cache

import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepo

class NoOpGitHubRepoCache : IGitHubRepoCache {
    override suspend fun saveSearchResultCache(query: String, repos: List<GitHubRepo>) = Unit
    override suspend fun getCachedSearchResult(query: String): List<GitHubRepo> = emptyList()
    override suspend fun clearCacheQuery(query: String) = Unit
}

