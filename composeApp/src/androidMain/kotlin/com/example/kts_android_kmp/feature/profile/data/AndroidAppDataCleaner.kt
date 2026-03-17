package com.example.kts_android_kmp.feature.profile.data

import com.example.kts_android_kmp.db.AppDatabase
import com.example.kts_android_kmp.feature.profile.domain.IAppDataCleaner

class AndroidAppDataCleaner(
    private val db: AppDatabase,
) : IAppDataCleaner {
    override suspend fun clearAll() {
        db.clearAllTables()
    }
}

