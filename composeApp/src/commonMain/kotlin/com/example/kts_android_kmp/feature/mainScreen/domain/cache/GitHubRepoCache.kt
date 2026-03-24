package com.example.kts_android_kmp.feature.mainScreen.domain.cache

import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepo

interface GitHubRepoCache {
    suspend fun saveSearchResultCache(query: String, repos: List<GitHubRepo>)

    suspend fun getCachedSearchResult(query: String): List<GitHubRepo>

    suspend fun clearCacheQuery(query: String)
}

