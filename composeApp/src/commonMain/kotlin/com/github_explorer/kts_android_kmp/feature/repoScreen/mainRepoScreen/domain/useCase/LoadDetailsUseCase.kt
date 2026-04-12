package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoDetails
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoRepository

class LoadDetailsUseCase(
    private val repoRepository: GithubRepoRepository
) {
    suspend fun loadRepoDetails(owner: String, repo: String): Result<GithubRepoDetails> {
        return repoRepository.loadCurRepoDetails(owner, repo)
    }
}