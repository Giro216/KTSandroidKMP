package com.example.kts_android_kmp.storage.platform

import com.example.kts_android_kmp.storage.domain.IAppPreferences
import kotlinx.coroutines.flow.Flow

expect class PlatformPreferences : IAppPreferences {
    override fun getBoolean(key: String, default: Boolean): Flow<Boolean>
    override suspend fun putBoolean(key: String, value: Boolean)

    override fun getString(key: String, default: String?): Flow<String?>
    override suspend fun putString(key: String, value: String?)
}

