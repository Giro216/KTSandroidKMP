package com.github_explorer.kts_android_kmp.feature.repoScreen.data.repo

import com.github_explorer.kts_android_kmp.core.data.network.GitHubApi
import com.github_explorer.kts_android_kmp.feature.repoScreen.data.network.GithubRepoDetailsDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.data.network.GithubRepoReadmeDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.GithubRepoDetails
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.GithubRepoReadme
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.GithubRepoRepository
import com.github_explorer.kts_android_kmp.utils.coRunCatching
import kotlin.io.encoding.Base64
import kotlin.time.Instant

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
    // GitHub API возвращает Base64 с перевода строк, нужно удалить все whitespace
    val cleanContent = content.replace("\\s".toRegex(), "")
    val decodedContent = Base64.decode(cleanContent).decodeToString()

    return GithubRepoReadme(
        decodeContent = decodedContent,
        path = name,
        htmlUrl = null, // при необходимости можно добавить поле из API в DTO
    )
}

private fun GithubRepoDetailsDto.toDomain(): GithubRepoDetails {
    return GithubRepoDetails(
        id = id,
        name = name,
        fullName = fullName,
        ownerName = owner.login,
        ownerAvatarUrl = owner.avatarUrl,
        description = description,
        htmlUrl = htmlUrl,
        starsCount = stargazersCount,
        forksCount = forksCount,
        openIssuesCount = openIssuesCount,
        watchersCount = watchersCount,
        language = language,
        licenseName = license?.name,
        topics = topics,
        homepage = homepage,
        defaultBranch = defaultBranch,
        createdAt = createdAt?.let { parseInstantOrNull(it) },
        updatedAt = updatedAt?.let { parseInstantOrNull(it) },
        pushedAt = pushedAt?.let { parseInstantOrNull(it) },
    )
}

private fun parseInstantOrNull(value: String): Instant? = try {
    Instant.parse(value)
} catch (_: Throwable) {
    null
}
