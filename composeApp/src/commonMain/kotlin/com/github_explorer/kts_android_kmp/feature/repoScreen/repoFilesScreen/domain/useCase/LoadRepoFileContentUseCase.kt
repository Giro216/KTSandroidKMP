package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileContent
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFilesRepository

class LoadRepoFileContentUseCase(
    private val repoFilesRepository: RepoFilesRepository,
) {
    suspend fun getFileContent(owner: String, repo: String, path: String): Result<RepoFileContent> {
        return repoFilesRepository.getFileContent(owner = owner, repo = repo, path = path)
    }
}