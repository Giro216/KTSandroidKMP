package com.github_explorer.kts_android_kmp.feature.settings.domain.useCase

import com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.platform.AppDataCleaner

class LogoutUseCase(
    private val appDataCleaner: AppDataCleaner,
) {
    suspend fun logout() {
        appDataCleaner.clearAll()
    }
}