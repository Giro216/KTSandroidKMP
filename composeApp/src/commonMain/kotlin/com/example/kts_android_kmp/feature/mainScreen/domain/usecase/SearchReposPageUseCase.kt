package com.example.kts_android_kmp.feature.mainScreen.domain.usecase

import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepository
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubSearchResult

class SearchReposPageUseCase(
    private val repo: GitHubRepository,
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

