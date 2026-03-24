package com.example.kts_android_kmp.storage.data.repo

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto
import com.example.kts_android_kmp.storage.domain.IAppPreferences
import com.example.kts_android_kmp.storage.domain.ISessionRepository
import com.example.kts_android_kmp.storage.domain.PrefKeys
import com.example.kts_android_kmp.utils.coRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val prefs: IAppPreferences,
) : ISessionRepository {

    override fun onboardingShown(): Flow<Boolean> =
        prefs.getBoolean(PrefKeys.ONBOARDING_SHOWN, default = false)

    override suspend fun setOnboardingShown(shown: Boolean): Result<Unit> = coRunCatching {
        prefs.putBoolean(PrefKeys.ONBOARDING_SHOWN, shown)
    }

    override suspend fun saveTokens(tokens: TokensModelDto): Result<Unit> = coRunCatching {
        prefs.saveTokens(tokens).getOrThrow()
    }

    override suspend fun clearTokens(): Result<Unit> = coRunCatching {
        prefs.clearTokens().getOrThrow()
    }

    override fun isLoggedIn(): Flow<Boolean> = accessToken().map { !it.isNullOrBlank() }

    override fun accessToken(): Flow<String?> =
        prefs.getString(PrefKeys.ACCESS_TOKEN, default = null)

}