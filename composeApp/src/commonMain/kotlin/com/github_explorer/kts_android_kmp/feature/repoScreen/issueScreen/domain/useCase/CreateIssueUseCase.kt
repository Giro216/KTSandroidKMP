package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssue
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssueRepository

class CreateIssueUseCase(
    private val issueRepository: GithubIssueRepository,
) {
    suspend fun createIssue(
        owner: String,
        repo: String,
        title: String,
        body: String,
        canCreateIssue: Boolean,
    ): CreateIssueResult {
        if (!canCreateIssue) {
            return CreateIssueResult.ValidationError(CreateIssueValidationError.NoPermission)
        }

        val normalizedTitle = title.trim()
        if (normalizedTitle.isBlank()) {
            return CreateIssueResult.ValidationError(CreateIssueValidationError.EmptyTitle)
        }

        val normalizedBody = body.trim().ifBlank { null }
        return issueRepository.createIssue(
            owner = owner,
            repo = repo,
            title = normalizedTitle,
            body = normalizedBody,
        ).fold(
            onSuccess = { issue -> CreateIssueResult.Success(issue) },
            onFailure = { throwable -> CreateIssueResult.Failure(throwable) },
        )
    }
}

sealed interface CreateIssueResult {
    data class Success(val issue: GithubIssue) : CreateIssueResult
    data class ValidationError(val reason: CreateIssueValidationError) : CreateIssueResult
    data class Failure(val throwable: Throwable) : CreateIssueResult
}

enum class CreateIssueValidationError {
    NoPermission,
    EmptyTitle,
}

