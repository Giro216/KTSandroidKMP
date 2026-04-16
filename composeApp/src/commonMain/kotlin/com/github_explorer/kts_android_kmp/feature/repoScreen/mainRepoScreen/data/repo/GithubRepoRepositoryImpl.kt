package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.data.repo

import com.github_explorer.kts_android_kmp.core.data.network.GitHubApi
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.data.network.GithubRepoDetailsDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.data.network.GithubRepoReadmeDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoDetails
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoReadme
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.domain.GithubRepoRepository
import com.github_explorer.kts_android_kmp.utils.coRunCatching
import kotlin.io.encoding.Base64

class GithubRepoRepositoryImpl(
    private val api: GitHubApi,
) : GithubRepoRepository {

    override suspend fun loadCurRepoReadme(owner: String, repo: String): Result<GithubRepoReadme> {
        return coRunCatching {
            api.getCurRepoReadme(owner, repo).toDomain()
        }
    }

    override suspend fun loadCurRepoDetails(
        owner: String,
        repo: String,
    ): Result<GithubRepoDetails> {
        return coRunCatching {
            api.getCurRepoDetails(owner, repo).toDomain()
        }
    }
}

private fun GithubRepoReadmeDto.toDomain(): GithubRepoReadme {
    val cleanContent = content.replace("\\s".toRegex(), "")
    val decodedContent = Base64.decode(cleanContent).decodeToString()

    return GithubRepoReadme(
        decodeContent = decodedContent,
        path = name,
        htmlUrl = htmlUrl,
        downloadUrl = downloadUrl,
    )
}

private fun GithubRepoDetailsDto.toDomain(): GithubRepoDetails {
    return GithubRepoDetails(
        id = id,
        owner = owner.login,
        name = name,
        fullName = fullName,
        ownerAvatarUrl = owner.avatarUrl,
        description = description,
        htmlUrl = htmlUrl,
        starsCount = stargazersCount,
        forksCount = forksCount,
        openIssuesCount = openIssuesCount,
        watchersCount = watchersCount,
        language = language,
        licenseName = license?.name,
        defaultBranch = defaultBranch,
        updatedAt = updatedAt,
    )
}

