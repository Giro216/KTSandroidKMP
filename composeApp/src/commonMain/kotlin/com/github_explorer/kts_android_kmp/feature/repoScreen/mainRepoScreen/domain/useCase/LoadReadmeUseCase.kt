package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoReadme
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoRepository

class LoadReadmeUseCase(
    private val repoRepository: GithubRepoRepository
) {
    suspend fun loadReadme(owner: String, repo: String): Result<GithubRepoReadme> {
        return repoRepository.loadCurRepoReadme(owner, repo)
    }
}