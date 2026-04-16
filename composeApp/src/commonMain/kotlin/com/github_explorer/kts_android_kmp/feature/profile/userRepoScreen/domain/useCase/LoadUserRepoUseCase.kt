package com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.domain.UserRepoRepository

class LoadUserRepoUseCase(
    private val userRepoRepository: UserRepoRepository,
) {
    suspend fun loadUserRepos(): Result<List<GitHubRepo>> {
        return userRepoRepository.loadUserRepos()
    }
}

