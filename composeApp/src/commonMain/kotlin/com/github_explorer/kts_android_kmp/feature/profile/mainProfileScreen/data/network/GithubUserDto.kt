package com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubUserDto(
    val login: String,
    val id: Long,
    @SerialName("avatar_url")
    val avatarUrl: String,
    val name: String? = null,
    val bio: String? = null,
    @SerialName("public_repos")
    val publicRepos: Int = 0,
    @SerialName("owned_private_repos")
    val ownedPrivateRepos: Int = 0,
    val followers: Int = 0,
)
