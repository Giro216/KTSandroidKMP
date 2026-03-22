package com.example.kts_android_kmp.feature.mainScreen.domain.usecase

import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubSearchResult
import com.example.kts_android_kmp.feature.mainScreen.domain.IGitHubRepository

class SearchReposPageUseCase(
    private val repo: IGitHubRepository,
) {
    suspend operator fun invoke(
        query: String,
        page: Int,
        perPage: Int,
    ): Result<GitHubSearchResult> {
        return repo.loadEntities(
            repo.initGitHubApiReposRequestParam(
                query = query,
                perPage = perPage,
                page = page,
            ),
        )
    }
}

