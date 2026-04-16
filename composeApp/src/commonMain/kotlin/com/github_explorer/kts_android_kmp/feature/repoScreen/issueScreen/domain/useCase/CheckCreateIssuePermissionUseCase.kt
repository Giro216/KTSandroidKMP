package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssueRepository

class CheckCreateIssuePermissionUseCase(
    private val issueRepository: GithubIssueRepository,
) {
    suspend fun canCreateIssue(owner: String): Result<Boolean> {
        return issueRepository.canCreateIssue(owner)
    }
}

