package com.github_explorer.kts_android_kmp.feature.repoScreen.domain

interface GithubRepoRepository {
    suspend fun loadCurRepoReadme(owner: String, repo: String): Result<GithubRepoReadme>

    suspend fun loadCurRepoDetails(owner: String, repo: String): Result<GithubRepoDetails>
}