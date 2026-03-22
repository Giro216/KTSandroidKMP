package com.example.kts_android_kmp.feature.profile.platform


interface IAppDataCleaner {
    suspend fun clearAll()
}

class NoOpAppDataCleaner : IAppDataCleaner {
    override suspend fun clearAll() = Unit
}
