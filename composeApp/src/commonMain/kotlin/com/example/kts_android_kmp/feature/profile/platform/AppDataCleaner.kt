package com.example.kts_android_kmp.feature.profile.platform


interface AppDataCleaner {
    suspend fun clearAll()
}

class NoOpAppDataCleanerImpl : AppDataCleaner {
    override suspend fun clearAll() = Unit
}
