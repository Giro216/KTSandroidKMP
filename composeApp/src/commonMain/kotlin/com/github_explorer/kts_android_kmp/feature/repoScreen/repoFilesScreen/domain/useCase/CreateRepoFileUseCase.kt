package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFilesRepository

class CreateRepoFileUseCase(
    private val repoFilesRepository: RepoFilesRepository,
) {
    suspend fun execute(
        owner: String,
        repo: String,
        path: String,
        contentUtf8: String,
        message: String,
    ): Result<Unit> {
        return repoFilesRepository.createFile(
            owner = owner,
            repo = repo,
            path = path,
            contentUtf8 = contentUtf8,
            message = message,
        )
    }
}

