package com.example.kts_android_kmp.feature.profile.domain.useCase

import com.example.kts_android_kmp.feature.profile.domain.IAppDataCleaner

class LogoutUseCase(
    private val appDataCleaner: IAppDataCleaner,
) {
    suspend fun logout() {
        appDataCleaner.clearAll()
    }
}
