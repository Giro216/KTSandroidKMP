package com.example.kts_android_kmp.storage.platform

import com.example.kts_android_kmp.storage.domain.IAppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSUserDefaults

actual class PlatformPreferences : IAppPreferences {

    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()

    actual override fun getBoolean(key: String, default: Boolean): Flow<Boolean> {
        return flow {
            val value = if (defaults.objectForKey(key) == null) default else defaults.boolForKey(key)
            emit(value)
        }
    }

    actual override suspend fun putBoolean(key: String, value: Boolean) {
        defaults.setBool(value, forKey = key)
    }

    actual override fun getString(key: String, default: String?): Flow<String?> {
        return flow {
            emit(defaults.stringForKey(key) ?: default)
        }
    }

    actual override suspend fun putString(key: String, value: String?) {
        if (value == null) defaults.removeObjectForKey(key) else defaults.setObject(value, forKey = key)
    }
}

