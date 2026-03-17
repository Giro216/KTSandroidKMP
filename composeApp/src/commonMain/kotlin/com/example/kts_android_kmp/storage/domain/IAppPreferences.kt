package com.example.kts_android_kmp.storage.domain

import kotlinx.coroutines.flow.Flow

interface IAppPreferences {
    fun getBoolean(key: String, default: Boolean = false): Flow<Boolean>
    suspend fun putBoolean(key: String, value: Boolean)

    fun getString(key: String, default: String? = null): Flow<String?>
    suspend fun putString(key: String, value: String?)
}

object PrefKeys {
    const val ONBOARDING_SHOWN = "onboarding_shown"
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    const val ID_TOKEN = "id_token"
}

