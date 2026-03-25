package com.github_explorer.kts_android_kmp.feature.profile.data

import com.github_explorer.kts_android_kmp.core.data.storage.domain.SessionRepository
import com.github_explorer.kts_android_kmp.db.AppDatabase
import com.github_explorer.kts_android_kmp.feature.profile.platform.AppDataCleaner
import io.github.aakira.napier.Napier

class AndroidAppDataCleanerImpl(
    private val db: AppDatabase,
    private val sessionRepository: SessionRepository,

    ) : AppDataCleaner {
    override suspend fun clearAll() {
        db.clearAllTables()
        sessionRepository.setOnboardingShown(false)
            .onFailure { Napier.e("Failed to reset onboarding flag: ${it.message}", it) }

        sessionRepository.clearTokens()
            .onFailure { Napier.e("Failed to clear tokens: ${it.message}", it) }
    }
}

