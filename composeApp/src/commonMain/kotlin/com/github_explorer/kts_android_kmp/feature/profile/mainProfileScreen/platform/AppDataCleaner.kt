package com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.platform


interface AppDataCleaner {
    suspend fun clearAll()
}

class NoOpAppDataCleanerImpl : AppDataCleaner {
    override suspend fun clearAll() = Unit
}
