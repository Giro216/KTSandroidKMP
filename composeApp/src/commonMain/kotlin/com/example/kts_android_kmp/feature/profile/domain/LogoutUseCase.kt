package com.example.kts_android_kmp.feature.profile.domain

import com.example.kts_android_kmp.storage.domain.ISessionRepository

class LogoutUseCase(
    private val sessionRepository: ISessionRepository,
    private val appDataCleaner: IAppDataCleaner,
) {
    suspend fun logout() {
        appDataCleaner.clearAll()
        sessionRepository.setOnboardingShown(false)
        sessionRepository.clearTokens()
    }
}
