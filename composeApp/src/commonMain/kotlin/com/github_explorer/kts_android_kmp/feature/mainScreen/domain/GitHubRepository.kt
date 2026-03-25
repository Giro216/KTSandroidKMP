package com.github_explorer.kts_android_kmp.feature.mainScreen.domain

import com.github_explorer.kts_android_kmp.core.data.network.GitHubApiImpl

interface GitHubRepository {
    suspend fun loadEntities(param: GitHubApiImpl.LoadReposRequestParam): Result<GitHubSearchResult>

    fun initGitHubApiReposRequestParam(
        query: String,
        sort: GitHubApiImpl.LoadReposRequestParam.SortType? = GitHubApiImpl.LoadReposRequestParam.SortType.STARS,
        order: String? = "desc",
        perPage: Int? = 30,
        page: Int? = 1,
    ): GitHubApiImpl.LoadReposRequestParam
}