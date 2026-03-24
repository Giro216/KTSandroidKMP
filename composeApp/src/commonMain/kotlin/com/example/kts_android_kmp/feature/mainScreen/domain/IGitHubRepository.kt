package com.example.kts_android_kmp.feature.mainScreen.domain

import com.example.kts_android_kmp.core.data.network.GitHubApi

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