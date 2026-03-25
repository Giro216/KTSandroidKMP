package com.github_explorer.kts_android_kmp.feature.profile.domain.useCase

import com.github_explorer.kts_android_kmp.feature.profile.platform.AppDataCleaner

class LogoutUseCase(
    private val appDataCleaner: AppDataCleaner,
) {
    suspend fun logout() {
        appDataCleaner.clearAll()
    }
}
