package com.example.kts_android_kmp.feature.profile.data

import com.example.kts_android_kmp.core.data.storage.domain.ISessionRepository
import com.example.kts_android_kmp.db.AppDatabase
import com.example.kts_android_kmp.feature.profile.platform.IAppDataCleaner
import io.github.aakira.napier.Napier

class AndroidAppDataCleaner(
    private val db: AppDatabase,
    private val sessionRepository: ISessionRepository,

    ) : IAppDataCleaner {
    override suspend fun clearAll() {
        db.clearAllTables()
        sessionRepository.setOnboardingShown(false)
            .onFailure { Napier.e("Failed to reset onboarding flag: ${it.message}", it) }

        sessionRepository.clearTokens()
            .onFailure { Napier.e("Failed to clear tokens: ${it.message}", it) }
    }
}

