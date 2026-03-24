package com.example.kts_android_kmp.feature.profile.domain.useCase

import com.example.kts_android_kmp.feature.profile.domain.ProfileRepository
import com.example.kts_android_kmp.feature.profile.domain.UserProfile

class LoadUseCase(
    private val profileRepository: ProfileRepository,
) {
    suspend fun loadProfile(): Result<UserProfile?> {
        return profileRepository.loadProfile()
    }
}