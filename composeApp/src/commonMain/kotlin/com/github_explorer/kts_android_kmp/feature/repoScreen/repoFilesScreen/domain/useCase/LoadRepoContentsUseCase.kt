package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.useCase

import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoDirItem
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFilesRepository

class LoadRepoContentsUseCase(
    private val repoFilesRepository: RepoFilesRepository,
) {
    suspend fun execute(owner: String, repo: String, path: String): Result<List<RepoDirItem>> {
        return repoFilesRepository.listContents(owner = owner, repo = repo, path = path)
    }
}

