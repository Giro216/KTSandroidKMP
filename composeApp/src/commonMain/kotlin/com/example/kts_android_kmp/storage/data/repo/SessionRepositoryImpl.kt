package com.example.kts_android_kmp.storage.data.repo

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel
import com.example.kts_android_kmp.storage.domain.IAppPreferences
import com.example.kts_android_kmp.storage.domain.ISessionRepository
import com.example.kts_android_kmp.storage.domain.PrefKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val prefs: IAppPreferences,
) : ISessionRepository {

    override fun onboardingShown(): Flow<Boolean> =
        prefs.getBoolean(PrefKeys.ONBOARDING_SHOWN, default = false)

    override suspend fun setOnboardingShown(shown: Boolean) {
        prefs.putBoolean(PrefKeys.ONBOARDING_SHOWN, shown)
    }

    override suspend fun saveTokens(tokens: TokensModel) {
        prefs.putString(PrefKeys.ACCESS_TOKEN, tokens.accessToken)
        prefs.putString(PrefKeys.REFRESH_TOKEN, tokens.refreshToken)
        prefs.putString(PrefKeys.ID_TOKEN, tokens.idToken)
    }

    override suspend fun clearTokens() {
        prefs.putString(PrefKeys.ACCESS_TOKEN, null)
        prefs.putString(PrefKeys.REFRESH_TOKEN, null)
        prefs.putString(PrefKeys.ID_TOKEN, null)
    }

    override fun isLoggedIn(): Flow<Boolean> = accessToken().map { !it.isNullOrBlank() }

    override fun accessToken(): Flow<String?> =
        prefs.getString(PrefKeys.ACCESS_TOKEN, default = null)

}