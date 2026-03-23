package com.example.kts_android_kmp.storage.keyvalue

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel
import com.example.kts_android_kmp.storage.domain.IAppPreferences
import com.example.kts_android_kmp.storage.domain.PrefKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PlatformPreferences(
    private val storage: DataStoreKeyValueStorage = DataStoreKeyValueStorage(),
) : IAppPreferences {

    override fun getBoolean(key: String, default: Boolean): Flow<Boolean> =
        when (key) {
            PrefKeys.ONBOARDING_SHOWN -> storage.observeOnboardingShown()
            else -> flowOf(default)
        }

    override suspend fun putBoolean(key: String, value: Boolean) {
        when (key) {
            PrefKeys.ONBOARDING_SHOWN -> storage.setOnboardingShown(value).getOrThrow()
        }
    }

    override fun getString(key: String, default: String?): Flow<String?> =
        when (key) {
            PrefKeys.ACCESS_TOKEN -> storage.observeAccessToken()
            PrefKeys.REFRESH_TOKEN -> storage.observeRefreshToken()
            PrefKeys.ID_TOKEN -> storage.observeIdToken()
            else -> flowOf(default)
        }

    override suspend fun putString(key: String, value: String?) {
        if (value != null) {
            when (key) {
                PrefKeys.ACCESS_TOKEN -> storage.saveAccessToken(value).getOrThrow()
                PrefKeys.REFRESH_TOKEN -> storage.saveRefreshToken(value).getOrThrow()
                PrefKeys.ID_TOKEN -> storage.saveIdToken(value).getOrThrow()
            }
        } else {
            when (key) {
                PrefKeys.ACCESS_TOKEN -> storage.clearAccessToken().getOrThrow()
                PrefKeys.REFRESH_TOKEN -> storage.clearRefreshToken().getOrThrow()
                PrefKeys.ID_TOKEN -> storage.clearIdToken().getOrThrow()
            }
        }
    }

    override suspend fun saveTokens(tokens: TokensModel): Result<Unit> =
        storage.saveTokens(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            idToken = tokens.idToken,
        )

    override suspend fun clearTokens(): Result<Unit> = storage.clearTokens()
}

