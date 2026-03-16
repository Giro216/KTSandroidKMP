package com.example.kts_android_kmp.storage.platform

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.kts_android_kmp.storage.DataStoreProvider
import com.example.kts_android_kmp.storage.domain.IAppPreferences
import kotlinx.coroutines.flow.Flow

actual class PlatformPreferences(
) : IAppPreferences {

    actual override fun getBoolean(key: String, default: Boolean): Flow<Boolean> {
        val prefKey = booleanPreferencesKey(key)
        return DataStoreProvider.read(prefKey, default)
    }

    actual override suspend fun putBoolean(key: String, value: Boolean) {
        val prefKey = booleanPreferencesKey(key)
        DataStoreProvider.save(prefKey, value)
    }

    actual override fun getString(key: String, default: String?): Flow<String?> {
        val prefKey = stringPreferencesKey(key)
        return DataStoreProvider.read(prefKey, default ?: "")
    }

    actual override suspend fun putString(key: String, value: String?) {
        val prefKey = stringPreferencesKey(key)
        if (value == null) {
            DataStoreProvider.remove(prefKey)
        } else {
            DataStoreProvider.save(prefKey, value)
        }
    }
}


