package com.example.kts_android_kmp.feature.profile.domain

class LogoutUseCase(
    private val appDataCleaner: IAppDataCleaner,
) {
    suspend fun logout() {
        appDataCleaner.clearAll()
    }
}
