package com.github_explorer.kts_android_kmp.core.data.storage.domain

import com.github_explorer.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto
import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    fun getBoolean(key: String, default: Boolean = false): Flow<Boolean>
    suspend fun putBoolean(key: String, value: Boolean)

    fun getString(key: String, default: String? = null): Flow<String?>
    suspend fun putString(key: String, value: String?)

    suspend fun saveTokens(tokens: TokensModelDto): Result<Unit>
    suspend fun clearTokens(): Result<Unit>
}

object PrefKeys {
    const val ONBOARDING_SHOWN = "onboarding_shown"
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    const val ID_TOKEN = "id_token"
}

