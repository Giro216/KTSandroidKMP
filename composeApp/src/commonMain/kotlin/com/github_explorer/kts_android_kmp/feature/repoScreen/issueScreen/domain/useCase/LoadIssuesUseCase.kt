package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssue
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssueRepository

class LoadIssuesUseCase(
    private val issueRepository: GithubIssueRepository,
) {
    suspend fun loadIssues(owner: String, repo: String): Result<List<GithubIssue>> {
        return issueRepository.getIssues(owner = owner, repo = repo)
    }
}

