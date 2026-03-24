package com.example.kts_android_kmp.feature.profile.domain.useCase

import com.example.kts_android_kmp.feature.profile.platform.AppDataCleaner

class LogoutUseCase(
    private val appDataCleaner: AppDataCleaner,
) {
    suspend fun logout() {
        appDataCleaner.clearAll()
    }
}
