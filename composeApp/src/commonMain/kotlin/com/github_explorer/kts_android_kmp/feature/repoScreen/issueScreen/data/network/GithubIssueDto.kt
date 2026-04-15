package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.data.network

import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.domain.GithubIssue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubIssueDto(
    val id: Long,
    val number: Int,
    val title: String,
    val state: String,
    @SerialName("pull_request")
    val pullRequest: PullRequestMarkerDto? = null,
)

@Serializable
data class PullRequestMarkerDto(
    @SerialName("html_url")
    val htmlUrl: String? = null,
)

internal fun GithubIssueDto.toDomain(): GithubIssue {
    val mappedState = when (state.lowercase()) {
        "open" -> GithubIssue.State.OPEN
        "closed" -> GithubIssue.State.CLOSED
        else -> GithubIssue.State.UNKNOWN
    }

    return GithubIssue(
        id = id,
        number = number,
        title = title,
        state = mappedState,
    )
}

internal fun List<GithubIssueDto>.toDomain(): List<GithubIssue> {
    // GitHub API /issues возвращает и issues и PR. Для экрана issues исключаем PR
    return this
        .asSequence()
        .filter { it.pullRequest == null }
        .map { it.toDomain() }
        .toList()
}