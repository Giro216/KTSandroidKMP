package com.github_explorer.kts_android_kmp.feature.profile.userRepoScreen.domain

import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.GitHubRepo

interface UserRepoRepository {
    suspend fun loadUserRepos(): Result<List<GitHubRepo>>
}