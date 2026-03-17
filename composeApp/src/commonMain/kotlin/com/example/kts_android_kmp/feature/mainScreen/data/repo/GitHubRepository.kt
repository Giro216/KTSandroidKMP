package com.example.kts_android_kmp.feature.mainScreen.data.repo

import com.example.kts_android_kmp.feature.mainScreen.data.network.GitHubApi
import com.example.kts_android_kmp.feature.mainScreen.data.network.GithubRepoDto
import com.example.kts_android_kmp.feature.mainScreen.data.network.IGitHubApi
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepoEntity
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubSearchResult
import com.example.kts_android_kmp.feature.mainScreen.domain.IGitHubRepository
import com.example.kts_android_kmp.feature.mainScreen.domain.cache.IGitHubRepoCache
import com.example.kts_android_kmp.utils.coRunCatching

class GitHubRepositoryImpl(
    private val api: IGitHubApi,
    private val cache: IGitHubRepoCache,
) : IGitHubRepository {
    override suspend fun loadEntities(param: GitHubApi.LoadReposRequestParam): Result<GitHubSearchResult> {
        val query = param.query

        val networkResult = coRunCatching {
            val response = api.loadRepos(param)
            val items = response.items.map { it.toEntity() }

            coRunCatching { cache.saveSearchResultCache(query = query, repos = items) }

            GitHubSearchResult(
                totalCount = response.totalCount,
                items = items,
            )
        }

        if (networkResult.isSuccess) return networkResult

        val cached = coRunCatching { cache.getCachedSearchResult(query) }.getOrDefault(emptyList())
        return if (cached.isNotEmpty()) {
            Result.success(GitHubSearchResult(totalCount = cached.size, items = cached))
        } else {
            networkResult
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
