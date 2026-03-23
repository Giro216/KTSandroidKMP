package com.example.kts_android_kmp.feature.profile.data

import com.example.kts_android_kmp.db.AppDatabase
import com.example.kts_android_kmp.feature.profile.domain.IAppDataCleaner
import com.example.kts_android_kmp.storage.domain.ISessionRepository

class AndroidAppDataCleaner(
    private val db: AppDatabase,
    private val sessionRepository: ISessionRepository,

    ) : IAppDataCleaner {
    override suspend fun clearAll() {
        db.clearAllTables()
        sessionRepository.setOnboardingShown(false)
        sessionRepository.clearTokens()
    }
}

