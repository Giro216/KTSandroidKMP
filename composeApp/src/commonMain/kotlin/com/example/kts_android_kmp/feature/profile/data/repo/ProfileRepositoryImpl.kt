package com.example.kts_android_kmp.feature.profile.data.repo

import com.example.kts_android_kmp.core.data.network.GitHubApi
import com.example.kts_android_kmp.feature.profile.data.network.GithubUserDto
import com.example.kts_android_kmp.feature.profile.domain.ProfileRepository
import com.example.kts_android_kmp.feature.profile.domain.UserProfile
import com.example.kts_android_kmp.utils.coRunCatching

class ProfileRepositoryImpl(
    private val api: GitHubApi,
) : ProfileRepository {

    override suspend fun loadProfile(): Result<UserProfile> {
        return coRunCatching {
            api.getCurrentUser().toDomain()
        }
    }
}

private fun GithubUserDto.toDomain(): UserProfile {
    return UserProfile(
        avatarUrl = avatarUrl,
        name = name?.takeIf { it.isNotBlank() } ?: login,
        bio = bio,
        publicRepos = publicRepos,
        followers = followers,
    )
}
