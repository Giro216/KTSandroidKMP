package com.github_explorer.kts_android_kmp.feature.repoScreen.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepoDetailsDto(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val description: String? = null,
    @SerialName("html_url") val htmlUrl: String,
    val language: String? = null,
    @SerialName("stargazers_count") val stargazersCount: Int,
    @SerialName("forks_count") val forksCount: Int,
    @SerialName("open_issues_count") val openIssuesCount: Int,
    @SerialName("watchers_count") val watchersCount: Int,
    val topics: List<String> = emptyList(),
    val homepage: String? = null,
    @SerialName("default_branch") val defaultBranch: String,
    @SerialName("updated_at") val updatedAt: String,
    val owner: OwnerDto,
    val license: LicenseDto? = null,
) {
    @Serializable
    data class OwnerDto(
        val login: String,
        @SerialName("avatar_url") val avatarUrl: String? = null,
    )

    @Serializable
    data class LicenseDto(
        val key: String,
        val name: String,
    )
}