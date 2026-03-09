package com.example.kts_android_kmp.feature.main.models

import com.example.kts_android_kmp.network.GitHubApi
import com.example.kts_android_kmp.network.GithubRepoDto

interface IGitHubRepository {
    suspend fun loadEntities(param: GitHubApi.LoadReposRequestParam): Result<GitHubSearchResult>
}

class GitHubRepositoryImpl(
    private val api: GitHubApi,
) : IGitHubRepository {
    override suspend fun loadEntities(param: GitHubApi.LoadReposRequestParam): Result<GitHubSearchResult> {
        return runCatching {
            val response = api.loadRepos(param)
            GitHubSearchResult(
                totalCount = response.totalCount,
                items = response.items.map { it.toEntity() },
            )
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
