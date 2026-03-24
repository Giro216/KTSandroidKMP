package com.example.kts_android_kmp.feature.mainScreen.data.repo

import com.example.kts_android_kmp.core.data.network.GitHubApi
import com.example.kts_android_kmp.core.data.network.GitHubApiImpl
import com.example.kts_android_kmp.feature.mainScreen.data.network.GithubRepoDto
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepository
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubSearchResult
import com.example.kts_android_kmp.feature.mainScreen.domain.cache.GitHubRepoCache
import com.example.kts_android_kmp.utils.coRunCatching

class GitHubRepositoryImpl(
    private val api: GitHubApi,
    private val cache: GitHubRepoCache,
) : GitHubRepository {
    override suspend fun loadEntities(param: GitHubApiImpl.LoadReposRequestParam): Result<GitHubSearchResult> {
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
        sort: GitHubApiImpl.LoadReposRequestParam.SortType?,
        order: String?,
        perPage: Int?,
        page: Int?,
    ): GitHubApiImpl.LoadReposRequestParam {
        return GitHubApiImpl.LoadReposRequestParam(
            query = query,
            sort = sort,
            order = order,
            perPage = perPage,
            page = page,
        )
    }
}

private fun GithubRepoDto.toEntity(): GitHubRepo {
    return GitHubRepo(
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
