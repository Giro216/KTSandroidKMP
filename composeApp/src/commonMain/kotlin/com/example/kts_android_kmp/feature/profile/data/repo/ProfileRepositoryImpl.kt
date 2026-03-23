package com.example.kts_android_kmp.feature.profile.data.repo

import com.example.kts_android_kmp.feature.profile.data.network.GithubUserDto
import com.example.kts_android_kmp.feature.profile.domain.IProfileRepository
import com.example.kts_android_kmp.feature.profile.domain.UserProfileEntity
import com.example.kts_android_kmp.network.IGitHubApi
import com.example.kts_android_kmp.utils.coRunCatching

class ProfileRepositoryImpl(
    private val api: IGitHubApi,
) : IProfileRepository {

    override suspend fun loadProfile(): Result<UserProfileEntity> {
        return coRunCatching {
            api.getCurrentUser().toDomain()
        }
    }
}

private fun GithubUserDto.toDomain(): UserProfileEntity {
    return UserProfileEntity(
        avatarUrl = avatarUrl,
        name = name?.takeIf { it.isNotBlank() } ?: login,
        bio = bio,
        publicRepos = publicRepos,
        followers = followers,
    )
}
