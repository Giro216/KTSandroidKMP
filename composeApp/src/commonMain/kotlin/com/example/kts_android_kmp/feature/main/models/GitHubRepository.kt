package com.example.kts_android_kmp.feature.main.models

import com.example.kts_android_kmp.network.GitHubApi
import com.example.kts_android_kmp.network.GithubRepoDto

class GitHubRepository(
    private val api: GitHubApi,
) {
    suspend fun loadEntities(param: GitHubApi.LoadReposRequestParam): Result<List<GitHubRepoEntity>> {
        return runCatching {
            api.loadRepos(param).map { it.toEntity() }
        }
    }
}

private fun GithubRepoDto.toEntity(): GitHubRepoEntity {
    return GitHubRepoEntity(
        id = id,
        owner = owner.login,
        name = name,
        description = description,
        language = language,
        stars = stargazersCount,
        forks = forksCount,
        updatedAt = updatedAt,
    )
}
