package com.github_explorer.kts_android_kmp.app

import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileItemType
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object Bootstrap : Routes()

    @Serializable
    data object HelloScreen : Routes()

    @Serializable
    data object LoginScreen : Routes()

    @Serializable
    data object MainScreen : Routes()

    @Serializable
    data class RepoScreen(
        val owner: String,
        val repo: String,
    ) : Routes()

    @Serializable
    data class IssueScreen(
        val owner: String,
        val repo: String,
    ) : Routes()

    @Serializable
    data class RepoFilesScreen(
        val owner: String,
        val repo: String,
        val path: String,
        val type: RepoFileItemType,
    ) : Routes()
}
