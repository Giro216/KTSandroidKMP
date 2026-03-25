package com.github_explorer.kts_android_kmp.feature.profile.domain.useCase

import com.github_explorer.kts_android_kmp.feature.profile.domain.ProfileRepository
import com.github_explorer.kts_android_kmp.feature.profile.domain.UserProfile

class LoadUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend fun loadProfile(): Result<UserProfile?> {
        return profileRepository.loadProfile()
    }
}