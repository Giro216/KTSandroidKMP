package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain

interface RepoFilesRepository {
    suspend fun listContents(owner: String, repo: String, path: String): Result<List<RepoDirItem>>

    suspend fun getFileContent(owner: String, repo: String, path: String): Result<RepoFileContent>

    suspend fun createFile(
        owner: String,
        repo: String,
        path: String,
        contentUtf8: String,
        message: String,
    ): Result<Unit>

    suspend fun updateFile(
        owner: String,
        repo: String,
        path: String,
        contentUtf8: String,
        message: String,
    ): Result<Unit>
}