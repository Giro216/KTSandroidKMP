package com.example.kts_android_kmp.feature.profile.domain.useCase

import com.example.kts_android_kmp.feature.profile.domain.IProfileRepository
import com.example.kts_android_kmp.feature.profile.domain.UserProfile

class LoadUseCase(
    private val profileRepository: IProfileRepository,
) {
    suspend fun loadProfile(): Result<UserProfile?> {
        return profileRepository.loadProfile()
    }
}