package com.example.kts_android_kmp.feature.mainScreen.models

import com.example.kts_android_kmp.network.GitHubApi
import com.example.kts_android_kmp.network.IGitHubApi
import com.example.kts_android_kmp.utils.coRunCatching

interface IGitHubRepository {
    suspend fun loadEntities(param: GitHubApi.LoadReposRequestParam): Result<GitHubSearchResult>

    fun initGitHubApiReposRequestParam(
        query: String,
        sort: GitHubApi.LoadReposRequestParam.SortType? = GitHubApi.LoadReposRequestParam.SortType.STARS,
        order: String? = "desc",
        perPage: Int? = 30,
        page: Int? = 1,
    ): GitHubApi.LoadReposRequestParam
}

class GitHubRepositoryImpl(
    private val api: IGitHubApi,
) : IGitHubRepository {
    override suspend fun loadEntities(param: GitHubApi.LoadReposRequestParam): Result<GitHubSearchResult> {
        return coRunCatching {
            val response = api.loadRepos(param)
            GitHubSearchResult(
                totalCount = response.totalCount,
                items = response.items.map { it.toEntity() },
            )
        }
    }

    override fun initGitHubApiReposRequestParam(
        query: String,
        sort: GitHubApi.LoadReposRequestParam.SortType?,
        order: String?,
        perPage: Int?,
        page: Int?,
    ): GitHubApi.LoadReposRequestParam {
        return GitHubApi.LoadReposRequestParam(
            query = query,
            sort = sort,
            order = order,
            perPage = perPage,
            page = page,
        )
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
