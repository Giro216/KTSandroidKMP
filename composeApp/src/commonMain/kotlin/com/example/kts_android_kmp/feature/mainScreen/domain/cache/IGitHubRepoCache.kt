package com.example.kts_android_kmp.feature.mainScreen.domain.cache

import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepoEntity

interface IGitHubRepoCache {
    suspend fun saveSearchResultCache(query: String, repos: List<GitHubRepoEntity>)

    suspend fun getCachedSearchResult(query: String): List<GitHubRepoEntity>

    suspend fun clearCacheQuery(query: String)
}

